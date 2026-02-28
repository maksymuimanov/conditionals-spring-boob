package io.conditionals.condition;

import io.conditionals.condition.impl.OnCollectionPropertyCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnCollectionPropertyCondition.class)
public @interface ConditionalOnCollectionProperties {
    ConditionalOnCollectionProperty[] value();
}