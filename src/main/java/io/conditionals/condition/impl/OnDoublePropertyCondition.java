package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnDoubleProperties;
import io.conditionals.condition.ConditionalOnDoubleProperty;

import java.lang.annotation.Annotation;

public class OnDoublePropertyCondition extends NumericPropertySpringBootCondition<Double> {
    private static final double PRECISION = 0.000000001D;

    @Override
    protected boolean checkEquals(Double property, Double candidate) {
        return Math.abs(property - candidate) < PRECISION;
    }

    @Override
    protected boolean checkGreaterThan(Double property, Double candidate) {
        return property > candidate;
    }

    @Override
    protected boolean checkLessThan(Double property, Double candidate) {
        return property < candidate;
    }

    @Override
    protected boolean checkGreaterThanOrEqual(Double property, Double candidate) {
        return property >= candidate;
    }

    @Override
    protected boolean checkLessThanOrEqual(Double property, Double candidate) {
        return property <= candidate;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return ConditionalOnDoubleProperty.class;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationContainerClass() {
        return ConditionalOnDoubleProperties.class;
    }
}
