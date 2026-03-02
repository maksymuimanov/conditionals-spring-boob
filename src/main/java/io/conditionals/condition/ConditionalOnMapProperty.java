package io.conditionals.condition;

import io.conditionals.condition.impl.OnMapPropertyCondition;
import io.conditionals.condition.spec.MapMatchType;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnMapPropertyCondition.class)
@Repeatable(ConditionalOnMapProperties.class)
public @interface ConditionalOnMapProperty {
    String[] value() default {};

    String prefix() default "";

    String[] name() default {};

    String[] havingValue() default {};

    boolean not() default false;

    MapMatchType matchType() default MapMatchType.EQUALS;

    boolean matchIfMissing() default false;
}