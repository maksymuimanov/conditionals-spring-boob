package io.conditionals.condition;

import io.conditionals.condition.impl.OnLongPropertyCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnLongPropertyCondition.class)
public @interface ConditionalOnLongProperties {
    ConditionalOnLongProperty[] value();
}