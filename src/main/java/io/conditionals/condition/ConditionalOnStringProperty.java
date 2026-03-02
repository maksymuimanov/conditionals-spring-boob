package io.conditionals.condition;

import io.conditionals.condition.impl.OnStringPropertyCondition;
import io.conditionals.condition.spec.StringMatchType;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnStringPropertyCondition.class)
@Repeatable(ConditionalOnStringProperties.class)
public @interface ConditionalOnStringProperty {
    String[] value() default {};

    String prefix() default "";

    String[] name() default {};

    String havingValue() default "";

    boolean ignoreCase() default false;

    boolean trim() default false;

    boolean not() default false;

    StringMatchType matchType() default StringMatchType.EQUALS;

    boolean matchIfMissing() default false;
}