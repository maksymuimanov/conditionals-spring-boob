package io.conditionals.condition;

import io.conditionals.condition.impl.OnPortAvailableCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnPortAvailableCondition.class)
public @interface ConditionalOnPortAvailable {
    int[] value();
}