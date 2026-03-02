package io.conditionals.condition.impl;

import io.conditionals.condition.spec.ComparablePropertySpec;
import io.conditionals.condition.spec.PropertySpecMatcher;
import io.conditionals.condition.utils.ConditionUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.annotation.Annotation;

@SuppressWarnings("unchecked")
public abstract class ComparablePropertySpringBootCondition<V extends Comparable<T>, T> extends PropertySpringBootCondition<V, ComparablePropertySpec<V, T>> {
    protected boolean checkEquals(V property, V candidate) {
        return property.compareTo((T) candidate) == 0;
    }

    protected boolean checkGreaterThan(V property, V candidate) {
        return property.compareTo((T) candidate) > 0;
    }

    protected boolean checkLessThan(V property, V candidate) {
        return property.compareTo((T) candidate) < 0;
    }

    protected boolean checkGreaterThanOrEqual(V property, V candidate) {
        return property.compareTo((T) candidate) >= 0;
    }

    protected boolean checkLessThanOrEqual(V property, V candidate) {
        return property.compareTo((T) candidate) <= 0;
    }

    @Override
    protected ComparablePropertySpec<V, T> createSpec(@Nullable Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
        return new ComparablePropertySpec<>(annotationType, annotationAttributes);
    }

    @Override
    protected PropertySpecMatcher<V, ComparablePropertySpec<V, T>> createPropertySpecMatcher() {
        return new Matcher();
    }

    public class Matcher implements PropertySpecMatcher<V, ComparablePropertySpec<V, T>> {
        @Override
        public boolean compare(ComparablePropertySpec<V, T> spec, @Nullable V property, V candidate) {
            if (property == null) return false;
            boolean result = switch (spec.getMatchType()) {
                case EQUALS -> checkEquals(property, candidate);
                case GREATER_THAN -> checkGreaterThan(property, candidate);
                case LESS_THAN -> checkLessThan(property, candidate);
                case GREATER_THAN_OR_EQUAL -> checkGreaterThanOrEqual(property, candidate);
                case LESS_THAN_OR_EQUAL -> checkLessThanOrEqual(property, candidate);
            };
            return ConditionUtils.revert(result, spec.isNot());
        }
    }
}