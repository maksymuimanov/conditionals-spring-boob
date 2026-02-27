package io.conditionals.condition;

import io.conditionals.condition.impl.OnOsCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnOsCondition.class)
@Repeatable(ConditionalOnIntegerProperties.class)
public @interface ConditionalOnIntegerProperty {
    String[] value() default {};

    String prefix() default "";

    String[] name() default {};

    int havingValue() default 0;

    boolean not() default false;

    MatchType matchType() default MatchType.EQUALS;

    boolean matchIfMissing() default false;

    enum MatchType {
        EQUALS,
        POSITIVE,
        NEGATIVE,
        GREATER_THAN,
        LESS_THAN,
        GREATER_THAN_OR_EQUAL,
        LESS_THAN_OR_EQUAL
    }
}