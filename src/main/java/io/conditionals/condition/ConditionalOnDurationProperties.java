package io.conditionals.condition;

import io.conditionals.condition.impl.OnDurationPropertyCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnDurationPropertyCondition.class)
public @interface ConditionalOnDurationProperties {
    ConditionalOnDurationProperty[] value();
}