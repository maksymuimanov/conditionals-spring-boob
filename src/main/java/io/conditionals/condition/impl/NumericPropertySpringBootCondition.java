package io.conditionals.condition.impl;

import io.conditionals.condition.dto.NumericPropertySpec;
import io.conditionals.condition.dto.PropertySpecMatcher;
import io.conditionals.condition.utils.ConditionUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.annotation.Annotation;

public abstract class NumericPropertySpringBootCondition<V extends Number> extends PropertySpringBootCondition<V, NumericPropertySpec<V>> {
    @Override
    protected PropertySpecMatcher<V, NumericPropertySpec<V>> createPropertySpecMatcher() {
        return (spec, property, candidate) -> {
            if (property == null) return false;
            boolean result = switch (spec.getMatchType()) {
                case EQUALS -> this.checkEquals(property, candidate);
                case GREATER_THAN -> this.checkGreaterThan(property, candidate);
                case LESS_THAN -> this.checkLessThan(property, candidate);
                case GREATER_THAN_OR_EQUAL -> this.checkGreaterThanOrEqual(property, candidate);
                case LESS_THAN_OR_EQUAL -> this.checkLessThanOrEqual(property, candidate);
            };
            return ConditionUtils.revert(result, spec.isNot());
        };
    }

    protected abstract boolean checkEquals(V property, V candidate);

    protected abstract boolean checkGreaterThan(V property, V candidate);

    protected abstract boolean checkLessThan(V property, V candidate);

    protected abstract boolean checkGreaterThanOrEqual(V property, V candidate);

    protected abstract boolean checkLessThanOrEqual(V property, V candidate);

    @Override
    protected NumericPropertySpec<V> createSpec(@Nullable Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
        return new NumericPropertySpec<>(annotationType, annotationAttributes);
    }
}