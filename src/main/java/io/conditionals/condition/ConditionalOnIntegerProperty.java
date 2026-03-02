package io.conditionals.condition;

import io.conditionals.condition.impl.OnIntegerPropertyCondition;
import io.conditionals.condition.spec.ComparableMatchType;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;
/**
 * Declares a Spring Boot condition that matches based on integer-valued properties.
 *
 * <p>
 * When placed on a {@code @Configuration} class or {@code @Bean} method, Spring Boot evaluates the associated
 * {@link io.conditionals.condition.impl.OnIntegerPropertyCondition} during the condition evaluation phase of
 * configuration processing.
 * </p>
 *
 * <p><b>Semantics</b></p>
 * <ul>
 *     <li><b>Binding</b>: this annotation is backed by {@link io.conditionals.condition.impl.OnIntegerPropertyCondition}.</li>
 *     <li><b>Repeatable evaluation</b>: multiple instances (via {@link Repeatable} or {@link ConditionalOnIntegerProperties})
 *     are aggregated with AND semantics.</li>
 *     <li><b>Name resolution</b>: property keys are composed as {@code prefix + name} with prefix normalization
 *     (trim; append {@code '.'} when non-empty).</li>
 *     <li><b>Property name selection</b>: {@link #value()} and {@link #name()} are mutually exclusive; exactly one must
 *     be non-empty.</li>
 *     <li><b>Comparison</b>: the resolved property value is compared against {@link #havingValue()} using {@link #matchType()}.</li>
 *     <li><b>Negation</b>: the final predicate is {@code (comparisonResult XOR not)}.</li>
 *     <li><b>Missing behavior</b>: missing properties produce a no-match outcome unless {@link #matchIfMissing()} is {@code true}.</li>
 * </ul>
 *
 * <p><b>Thread Safety</b></p>
 * <p>This annotation is thread-safe. It declares immutable metadata.</p>
 *
 * <p><b>Null Handling</b></p>
 * <p>Annotation attributes are never {@code null}; resolved property values may be {@code null} and are treated as non-matching.</p>
 *
 * <p><b>Edge Cases</b></p>
 * <ul>
 *     <li>Type conversion failures when resolving integer properties are treated as non-matching.</li>
 *     <li>Empty {@link #value()} and {@link #name()} arrays are invalid and will result in an {@link IllegalStateException}
 *     during evaluation.</li>
 * </ul>
 *
 * <p><b>Performance Characteristics</b></p>
 * <p>Evaluation is linear in the number of configured property names and annotation instances.</p>
 *
 * <p><b>Usage Example</b></p>
 * <pre>{@code
 * @ConditionalOnIntegerProperty(prefix = "app", name = "port", havingValue = 8080)
 * @Configuration
 * class PortConfiguration {
 * }
 * }</pre>
 *
 * @author Maksym Uimanov
 * @since 1.0
 * @see io.conditionals.condition.impl.OnIntegerPropertyCondition
 * @see ConditionalOnIntegerProperties
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnIntegerPropertyCondition.class)
@Repeatable(ConditionalOnIntegerProperties.class)
public @interface ConditionalOnIntegerProperty {
    String[] value() default {};

    String prefix() default "";

    String[] name() default {};

    int havingValue() default 0;

    boolean not() default false;

    ComparableMatchType matchType() default ComparableMatchType.EQUALS;

    boolean matchIfMissing() default false;
}