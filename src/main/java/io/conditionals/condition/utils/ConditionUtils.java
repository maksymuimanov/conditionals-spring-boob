package io.conditionals.condition.utils;

import io.conditionals.condition.dto.PropertySpecMatcher;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Static utilities for evaluating Spring Boot {@link org.springframework.context.annotation.Condition} metadata.
 *
 * <p>
 * Provides shared evaluation helpers used by {@link org.springframework.boot.autoconfigure.condition.SpringBootCondition}
 * implementations in this project. These methods are intended to be invoked from
 * {@link org.springframework.boot.autoconfigure.condition.SpringBootCondition#getMatchOutcome(org.springframework.context.annotation.ConditionContext, AnnotatedTypeMetadata)}
 * during Spring Boot's condition evaluation pipeline.
 * </p>
 *
 * <p><b>Semantics</b></p>
 * <ul>
 *     <li><b>Attribute discovery</b>: If no annotation attributes are present for the queried annotation type,
 *     the evaluation yields a {@link ConditionOutcome#noMatch(String) no-match} outcome via
 *     {@link #noAttributesFound(ConditionMessage.Builder)}.</li>
 *     <li><b>Repeatable composition</b>: For repeatable annotations, evaluation is typically performed over a
 *     stream produced by {@link #mergedStream(AnnotatedTypeMetadata, Class, Class)}, which yields at most one
 *     direct annotation instance followed by any container instances.</li>
 *     <li><b>Aggregation</b>: {@link #evaluateConditions(ConditionMessage.Builder, Stream, Function)} evaluates each
 *     attribute instance independently and aggregates results with AND semantics: the overall outcome is a match
 *     only if all evaluated attribute instances match; otherwise the overall outcome is a no-match.</li>
 *     <li><b>Property evaluation</b>: {@link #evaluatePropertyConditions(ConditionMessage.Builder, AnnotationAttributes, BiFunction, PropertyResolver, PropertySpecMatcher)}
 *     evaluates the presence and value of each configured property name. Missing properties cause a no-match
 *     when {@code matchIfMissing=false}; non-matching values and conversion failures cause a no-match.</li>
 * </ul>
 *
 * <p><b>Thread Safety</b></p>
 * <p>
 * This class is thread-safe. It is stateless and all methods are pure with respect to shared state.
 * Thread-safety of the provided {@link PropertyResolver} and {@link AnnotatedTypeMetadata} instances is governed
 * by the Spring container.
 * </p>
 *
 * <p><b>Null Handling</b></p>
 * <ul>
 *     <li>{@code @Nullable} {@link AnnotationAttributes} inputs are treated as absent attributes and result in
 *     {@link #noAttributesFound(ConditionMessage.Builder)}.</li>
 *     <li>{@code @Nullable} elements in streams are evaluated via {@link #checkAttributes(ConditionMessage.Builder, AnnotationAttributes, Function)}
 *     and therefore produce a no-match outcome for that element.</li>
 * </ul>
 *
 * <p><b>Edge Cases</b></p>
 * <ul>
 *     <li>Empty attribute streams cause an immediate no-match outcome.</li>
 *     <li>Container annotation presence without any contained values is treated as an empty stream.</li>
 *     <li>Callers must ensure {@code outcomeFunction} and other functional parameters are non-null; otherwise a
 *     {@link NullPointerException} will be thrown by the JVM.</li>
 * </ul>
 *
 * <p><b>Performance Characteristics</b></p>
 * <p>
 * Evaluation is linear in the number of attribute instances and configured property names. Some methods
 * materialize the provided stream into a list to support empty-stream detection.
 * </p>
 *
 * <p><b>Usage Example</b></p>
 * <pre>{@code
 * ConditionMessage.Builder message = ConditionMessage.forCondition(MyConditional.class);
 * Stream<AnnotationAttributes> attrs = ConditionUtils.mergedStream(metadata, MyConditional.class, MyConditionals.class);
 * ConditionOutcome outcome = ConditionUtils.evaluateConditions(message, attrs, a -> evaluateOne(a));
 * }</pre>
 *
 * @author Maksym Uimanov
 * @since 1.0
 * @see org.springframework.boot.autoconfigure.condition.SpringBootCondition
 * @see ConditionOutcome
 * @see ConditionMessage
 */
public final class ConditionUtils {
    private static final String VALUE = "value";

    private ConditionUtils() {
    }

    /**
     * Builds a stream of attributes for a repeatable annotation by concatenating the direct annotation attributes
     * (if present) with the values obtained from the container annotation.
     *
     * <p><b>Semantics</b></p>
     * <ul>
     *     <li>First yields the direct annotation attributes (single instance) if present.</li>
     *     <li>Then yields each element contained in the container annotation in declared order.</li>
     *     <li>If the direct annotation is absent, only container values are yielded.</li>
     * </ul>
     *
     * <p><b>Thread Safety</b></p>
     * <p>Thread-safe as it does not mutate shared state.</p>
     *
     * <p><b>Null Handling</b></p>
     * <p>May yield an empty stream; never yields {@code null} unless the underlying metadata returns {@code null} container values.</p>
     *
     * @param metadata annotated type metadata
     * @param annotationClass repeatable annotation type
     * @param containerAnnotationClass container annotation type
     * @return concatenated stream of attribute instances
     */
    public static Stream<@Nullable AnnotationAttributes> mergedStream(AnnotatedTypeMetadata metadata,
                                                                      Class<? extends Annotation> annotationClass,
                                                                      Class<? extends Annotation> containerAnnotationClass) {
        AnnotationAttributes spec = attributes(metadata, annotationClass);
        Stream<@Nullable AnnotationAttributes> stream = stream(metadata, containerAnnotationClass);
        return spec == null
                ? stream
                : Stream.concat(Stream.of(spec), stream);
    }

    /**
     * Returns a stream over the values of a container annotation's {@code value} attribute.
     *
     * <p><b>Semantics</b></p>
     * <ul>
     *     <li>If the container annotation is not present on {@code metadata}, returns an empty stream.</li>
     *     <li>Otherwise, reads the container's {@code value} attribute and returns its elements as a stream.</li>
     * </ul>
     *
     * <p><b>Thread Safety</b></p>
     * <p>Thread-safe as it does not mutate shared state.</p>
     *
     * <p><b>Null Handling</b></p>
     * <p>If the annotation attributes map is {@code null}, an empty stream is returned.</p>
     *
     * <p><b>Edge Cases</b></p>
     * <p>If the {@code value} attribute is not an {@code AnnotationAttributes[]} as expected, a {@link ClassCastException} may occur.</p>
     *
     * @param metadata annotated type metadata
     * @param annotationClass container annotation type
     * @return stream of contained annotation attributes
     */
    public static Stream<@Nullable AnnotationAttributes> stream(AnnotatedTypeMetadata metadata,
                                                                Class<? extends Annotation> annotationClass) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(annotationClass.getName());
        if (attributes == null)
            return Stream.of();
        AnnotationAttributes[] annotationAttributes = (AnnotationAttributes[]) attributes.get(VALUE);
        return Arrays.stream(annotationAttributes);
    }

    /**
     * Reads annotation attributes for a single (non-container) annotation type.
     *
     * <p><b>Semantics</b></p>
     * <ul>
     *     <li>Delegates to {@link AnnotatedTypeMetadata#getAnnotationAttributes(String)} and wraps the map via
     *     {@link AnnotationAttributes#fromMap(Map)}.</li>
     *     <li>If the annotation is not present, returns {@code null}.</li>
     * </ul>
     *
     * <p><b>Thread Safety</b></p>
     * <p>Thread-safe as it does not mutate shared state.</p>
     *
     * <p><b>Null Handling</b></p>
     * <p>Returns {@code null} when attributes are absent.</p>
     *
     * @param metadata annotated type metadata
     * @param annotationClass annotation type
     * @return annotation attributes, or {@code null} if the annotation is not present
     */
    @Nullable
    public static AnnotationAttributes attributes(AnnotatedTypeMetadata metadata,
                                                  Class<? extends Annotation> annotationClass) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(annotationClass.getName());
        return AnnotationAttributes.fromMap(attributes);
    }

    /**
     * Creates a matching {@link ConditionOutcome} with a message composed by concatenating parts.
     *
     * <p><b>Semantics</b></p>
     * <p>Concatenates all {@code message} fragments without a delimiter.</p>
     *
     * <p><b>Thread Safety</b></p>
     * <p>Thread-safe.</p>
     *
     * <p><b>Null Handling</b></p>
     * <p>Passing {@code null} fragments will result in the literal string {@code "null"} in the composed message.</p>
     *
     * @param message message fragments
     * @return matching outcome
     */
    public static ConditionOutcome match(String... message) {
        return ConditionOutcome.match(String.join("", message));
    }

    /**
     * Creates a no-match {@link ConditionOutcome} indicating that no relevant annotation attributes were found.
     *
     * <p><b>Semantics</b></p>
     * <p>Produces {@link ConditionOutcome#noMatch(ConditionMessage)} with the provided builder.</p>
     *
     * <p><b>Thread Safety</b></p>
     * <p>Thread-safe.</p>
     *
     * <p><b>Null Handling</b></p>
     * <p>{@code message} must be non-null.</p>
     *
     * @param message condition message builder
     * @return no-match outcome
     */
    public static ConditionOutcome noAttributesFound(ConditionMessage.Builder message) {
        return noMatchBecause(message, "no annotation attributes were found");
    }

    /**
     * Creates a no-match {@link ConditionOutcome} with a reason appended to the provided message builder.
     *
     * <p><b>Semantics</b></p>
     * <p>Concatenates the {@code reason} fragments without a delimiter and uses them as the {@code because(...)} argument.</p>
     *
     * <p><b>Thread Safety</b></p>
     * <p>Thread-safe.</p>
     *
     * <p><b>Null Handling</b></p>
     * <p>Passing {@code null} fragments will result in the literal string {@code "null"} in the composed reason.</p>
     *
     * @param message condition message builder
     * @param reason reason fragments
     * @return no-match outcome
     */
    public static ConditionOutcome noMatchBecause(ConditionMessage.Builder message, String... reason) {
        return ConditionOutcome.noMatch(message.because(String.join("", reason)));
    }

    /**
     * Creates a no-match {@link ConditionOutcome} with a message composed by concatenating parts.
     *
     * <p><b>Semantics</b></p>
     * <p>Concatenates all {@code message} fragments without a delimiter.</p>
     *
     * <p><b>Thread Safety</b></p>
     * <p>Thread-safe.</p>
     *
     * <p><b>Null Handling</b></p>
     * <p>Passing {@code null} fragments will result in the literal string {@code "null"} in the composed message.</p>
     *
     * @param message message fragments
     * @return no-match outcome
     */
    public static ConditionOutcome noMatch(String... message) {
        return ConditionOutcome.noMatch(String.join("", message));
    }

    public static boolean revert(boolean condition, boolean revert) {
        return condition ^ revert;
    }
}
