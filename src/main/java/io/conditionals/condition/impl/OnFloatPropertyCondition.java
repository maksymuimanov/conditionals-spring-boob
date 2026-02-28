package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnFloatProperties;
import io.conditionals.condition.ConditionalOnFloatProperty;
import io.conditionals.condition.dto.MatchingPropertySpec;
import io.conditionals.condition.utils.ConditionUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

/**
 * Spring Boot {@link org.springframework.context.annotation.Condition} implementation backing
 * {@link io.conditionals.condition.ConditionalOnFloatProperty}.
 *
 * <p>
 * This condition is evaluated during Spring Boot's condition evaluation pipeline for configuration classes and
 * {@code @Bean} methods annotated with {@code @ConditionalOnFloatProperty}, including repeatable declarations via
 * {@code @ConditionalOnFloatProperties}.
 * </p>
 *
 * <p><b>Semantics</b></p>
 * <ul>
 *     <li><b>Attribute discovery</b>: annotation instances are obtained via
 *     {@link ConditionUtils#mergedStream(AnnotatedTypeMetadata, Class, Class)} in encounter order.</li>
 *     <li><b>Aggregation</b>: multiple annotation instances are aggregated using AND semantics.</li>
 *     <li><b>Property evaluation order</b>: within each annotation instance, configured property names are evaluated
 *     in encounter order.</li>
 *     <li><b>Comparison</b>: resolved float values are compared to {@code havingValue} according to {@code matchType}.</li>
 *     <li><b>Equals precision</b>: for {@code EQUALS}, the comparison uses an absolute tolerance of {@code 0.00001F}.</li>
 *     <li><b>NaN handling</b>: if the resolved property value is {@code null} or if either the resolved value or the
 *     candidate value is {@link Float#NaN}, the comparison yields {@code false}.</li>
 *     <li><b>Negation</b>: the comparison result is XOR'ed with {@code not}.</li>
 *     <li><b>Missing behavior</b>: a missing property yields no-match unless {@code matchIfMissing=true}.</li>
 * </ul>
 *
 * <p><b>Thread Safety</b></p>
 * <p>This class is thread-safe. It is stateless.</p>
 *
 * <p><b>Null Handling</b></p>
 * <ul>
 *     <li>Absent annotation attributes yield a no-match outcome.</li>
 *     <li>A resolved property value of {@code null} is treated as non-matching.</li>
 * </ul>
 *
 * <p><b>Edge Cases</b></p>
 * <ul>
 *     <li>Type conversion failures while resolving float properties are treated as non-matching.</li>
 *     <li>Infinity values follow Java floating-point ordering rules.</li>
 * </ul>
 *
 * <p><b>Performance Characteristics</b></p>
 * <p>Evaluation is O(n) in the number of configured property names.</p>
 *
 * <p><b>Usage Example</b></p>
 * <pre>{@code
 * @ConditionalOnFloatProperty(prefix = "app", name = "ratio", havingValue = 0.75f)
 * @Configuration
 * class RatioConfiguration {
 * }
 * }</pre>
 *
 * @author Maksym Uimanov
 * @since 1.0
 * @see io.conditionals.condition.ConditionalOnFloatProperty
 * @see io.conditionals.condition.ConditionalOnFloatProperties
 */
public class OnFloatPropertyCondition extends SpringBootCondition {
    private static final float PRECISION = 0.00001F;

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context,
                                            AnnotatedTypeMetadata metadata) {
        ConditionMessage.Builder message = ConditionMessage.forCondition(ConditionalOnFloatProperty.class);
        Stream<@Nullable AnnotationAttributes> annotationAttributes = ConditionUtils.mergedStream(metadata, ConditionalOnFloatProperty.class, ConditionalOnFloatProperties.class);
        return ConditionUtils.evaluateConditions(message, annotationAttributes, attributes ->
                ConditionUtils.evaluatePropertyConditions(message, attributes, Spec::new, context.getEnvironment(), (spec, property, candidate) -> {
                    if (property == null || Float.isNaN(candidate) || Float.isNaN(property)) return false;
                    boolean result = switch (spec.getMatchType()) {
                        case EQUALS -> Math.abs(property - candidate) < PRECISION;
                        case GREATER_THAN -> property > candidate;
                        case LESS_THAN -> property < candidate;
                        case GREATER_THAN_OR_EQUAL -> property >= candidate;
                        case LESS_THAN_OR_EQUAL -> property <= candidate;
                    };
                    return result ^ spec.isNot();
                })
        );
    }

    private static class Spec extends MatchingPropertySpec<Float, Spec, ConditionalOnFloatProperty.MatchType> {
        private Spec(Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
            super(annotationType, annotationAttributes);
        }
    }
}
