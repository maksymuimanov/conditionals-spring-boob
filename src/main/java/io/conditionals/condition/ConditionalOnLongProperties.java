package io.conditionals.condition;

import io.conditionals.condition.impl.OnLongPropertyCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * Container annotation for repeatable {@link ConditionalOnLongProperty} declarations.
 *
 * <p>
 * This annotation is used by Java's {@link Repeatable} mechanism and may also be declared explicitly. Spring Boot
 * evaluates its {@link #value()} elements via {@link OnLongPropertyCondition}
 * during the condition evaluation phase of configuration processing.
 * </p>
 *
 * <p><b>Semantics</b></p>
 * <ul>
 *     <li><b>Aggregation</b>: contained {@link ConditionalOnLongProperty} instances are aggregated with AND
 *     semantics together with any directly declared instances.</li>
 *     <li><b>Evaluation order</b>: contained values are evaluated in the declared array order. If a direct
 *     {@link ConditionalOnLongProperty} is also present, it is evaluated before container values.</li>
 * </ul>
 *
 * <p><b>Thread Safety</b></p>
 * <p>This annotation is thread-safe. It declares immutable metadata.</p>
 *
 * <p><b>Null Handling</b></p>
 * <p>{@link #value()} is never {@code null}.</p>
 *
 * <p><b>Edge Cases</b></p>
 * <p>An empty {@link #value()} array results in no evaluable attribute instances and therefore a no-match outcome.</p>
 *
 * @author Maksym Uimanov
 * @since 1.0
 * @see ConditionalOnLongProperty
 * @see OnLongPropertyCondition
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnLongPropertyCondition.class)
public @interface ConditionalOnLongProperties {
    ConditionalOnLongProperty[] value();
}