package io.conditionals.condition;

import io.conditionals.condition.dto.ComparableMatchType;
import io.conditionals.condition.impl.OnDurationPropertyCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnDurationPropertyCondition.class)
@Repeatable(ConditionalOnDurationProperties.class)
public @interface ConditionalOnDurationProperty {
    String[] value() default {};

    String prefix() default "";

    String[] name() default {};

    String havingValue() default "0";

    boolean not() default false;

    ComparableMatchType matchType() default ComparableMatchType.EQUALS;

    boolean matchIfMissing() default false;
}