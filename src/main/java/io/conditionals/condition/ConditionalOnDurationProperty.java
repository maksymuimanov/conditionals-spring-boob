package io.conditionals.condition;

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

    String havingValue() default "0ms";

    boolean not() default false;

    MatchType matchType() default MatchType.EQUALS;

    boolean matchIfMissing() default false;

    enum MatchType {
        EQUALS,
        GREATER_THAN,
        LESS_THAN,
        GREATER_THAN_OR_EQUAL,
        LESS_THAN_OR_EQUAL
    }
}