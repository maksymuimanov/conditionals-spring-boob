package io.conditionals.condition;

import io.conditionals.condition.impl.OnFloatPropertyCondition;
import io.conditionals.condition.spec.ComparableMatchType;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnFloatPropertyCondition.class)
@Repeatable(ConditionalOnFloatProperties.class)
public @interface ConditionalOnFloatProperty {
    String[] value() default {};

    String prefix() default "";

    String[] name() default {};

    float havingValue() default 0;

    boolean not() default false;

    ComparableMatchType matchType() default ComparableMatchType.EQUALS;

    boolean matchIfMissing() default false;
}