package io.conditionals.condition;

import io.conditionals.condition.impl.OnCollectionPropertyCondition;
import io.conditionals.condition.spec.CollectionMatchType;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnCollectionPropertyCondition.class)
@Repeatable(ConditionalOnCollectionProperties.class)
public @interface ConditionalOnCollectionProperty {
    String[] value() default {};

    String prefix() default "";

    String[] name() default {};

    String[] havingValue() default {};

    int size() default -1;

    boolean not() default false;

    CollectionMatchType matchType() default CollectionMatchType.EQUALS;

    boolean matchIfMissing() default false;
}