package io.conditionals.condition;

import io.conditionals.condition.dto.StringMatchType;
import io.conditionals.condition.impl.OnStringPropertyCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnStringPropertyCondition.class)
@Repeatable(ConditionalOnStringProperties.class)
public @interface ConditionalOnStringProperty {
    /**
     * Alias for {@link #name()}.
     *
     * <p><b>Semantics</b></p>
     * <p>Provides property names that will be evaluated in encounter order.</p>
     *
     * <p><b>Thread Safety</b></p>
     * <p>Thread-safe.</p>
     *
     * <p><b>Null Handling</b></p>
     * <p>Never {@code null}. Default is an empty array.</p>
     *
     * @return property names
     */
    String[] value() default {};

    /**
     * Prefix applied when constructing each property key.
     *
     * <p><b>Semantics</b></p>
     * <p>
     * The runtime key is {@code normalizedPrefix + name}, where {@code normalizedPrefix} is trimmed and, when
     * non-empty, ends with {@code '.'}.
     * </p>
     *
     * <p><b>Thread Safety</b></p>
     * <p>Thread-safe.</p>
     *
     * <p><b>Null Handling</b></p>
     * <p>Never {@code null}. Default is the empty string.</p>
     *
     * @return prefix for property keys
     */
    String prefix() default "";

    /**
     * Property names to evaluate.
     *
     * <p><b>Semantics</b></p>
     * <p>Mutually exclusive with {@link #value()}.</p>
     *
     * <p><b>Thread Safety</b></p>
     * <p>Thread-safe.</p>
     *
     * <p><b>Null Handling</b></p>
     * <p>Never {@code null}. Default is an empty array.</p>
     *
     * @return property names
     */
    String[] name() default {};

    /**
     * Candidate string value to compare against resolved property values.
     *
     * <p><b>Semantics</b></p>
     * <p>The candidate participates in normalization (case/trim) and is used by {@link #matchType()}.</p>
     *
     * <p><b>Thread Safety</b></p>
     * <p>Thread-safe.</p>
     *
     * <p><b>Null Handling</b></p>
     * <p>Never {@code null}. Default is the empty string.</p>
     *
     * @return required value
     */
    String havingValue() default "";

    /**
     * Whether to perform case-insensitive matching.
     *
     * <p><b>Semantics</b></p>
     * <p>If enabled, values are lower-cased using {@link java.util.Locale#ROOT} before any trimming.</p>
     *
     * <p><b>Thread Safety</b></p>
     * <p>Thread-safe.</p>
     *
     * @return {@code true} for case-insensitive matching
     */
    boolean ignoreCase() default false;

    /**
     * Whether to trim both property and candidate values before comparison.
     *
     * <p><b>Semantics</b></p>
     * <p>Trimming is applied after optional case normalization.</p>
     *
     * <p><b>Thread Safety</b></p>
     * <p>Thread-safe.</p>
     *
     * @return {@code true} to apply {@link String#trim()}
     */
    boolean trim() default false;

    /**
     * Whether to invert the match result.
     *
     * <p><b>Semantics</b></p>
     * <p>The final predicate is {@code (comparisonResult XOR not)}.</p>
     *
     * <p><b>Thread Safety</b></p>
     * <p>Thread-safe.</p>
     *
     * @return {@code true} to negate the comparison result
     */
    boolean not() default false;

    /**
     * String comparison mode.
     *
     * @return comparison mode
     */
    StringMatchType matchType() default StringMatchType.EQUALS;

    /**
     * Whether to consider missing properties as matching.
     *
     * <p><b>Semantics</b></p>
     * <p>If {@code true}, a missing property key does not contribute to a no-match outcome.</p>
     *
     * <p><b>Thread Safety</b></p>
     * <p>Thread-safe.</p>
     *
     * @return {@code true} to match when a property is missing
     */
    boolean matchIfMissing() default false;
}