package io.conditionals.condition;

import io.conditionals.condition.dto.ComparableMatchType;
import io.conditionals.condition.impl.OnDoublePropertyCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnDoublePropertyCondition.class)
@Repeatable(ConditionalOnDoubleProperties.class)
public @interface ConditionalOnDoubleProperty {
    String[] value() default {};

    String prefix() default "";

    String[] name() default {};

    double havingValue() default 0;

    boolean not() default false;

    ComparableMatchType matchType() default ComparableMatchType.EQUALS;

    boolean matchIfMissing() default false;
}