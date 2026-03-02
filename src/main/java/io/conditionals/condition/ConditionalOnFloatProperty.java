package io.conditionals.condition;

import io.conditionals.condition.dto.ComparableMatchType;
import io.conditionals.condition.impl.OnFloatPropertyCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnFloatPropertyCondition.class)
@Repeatable(ConditionalOnFloatProperties.class)
public @interface ConditionalOnFloatProperty {
    /**
     * Alias for {@link #name()}.
     *
     * @return property names
     */
    String[] value() default {};

    /**
     * Prefix applied when constructing each property key.
     *
     * @return property key prefix
     */
    String prefix() default "";

    /**
     * Property names to evaluate.
     *
     * @return property names
     */
    String[] name() default {};

    /**
     * Candidate float value used for comparison.
     *
     * @return required value
     */
    float havingValue() default 0;

    /**
     * Whether to invert the match result.
     *
     * @return {@code true} to negate the comparison result
     */
    boolean not() default false;

    /**
     * Float comparison mode.
     *
     * @return comparison mode
     */
    ComparableMatchType matchType() default ComparableMatchType.EQUALS;

    /**
     * Whether to consider missing properties as matching.
     *
     * @return {@code true} to match when a property is missing
     */
    boolean matchIfMissing() default false;
}