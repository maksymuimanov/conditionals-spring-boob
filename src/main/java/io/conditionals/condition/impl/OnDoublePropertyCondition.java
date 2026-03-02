package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnDoubleProperties;
import io.conditionals.condition.ConditionalOnDoubleProperty;

import java.lang.annotation.Annotation;

public class OnDoublePropertyCondition extends ComparablePropertySpringBootCondition<Double, Double> {
    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return ConditionalOnDoubleProperty.class;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationContainerClass() {
        return ConditionalOnDoubleProperties.class;
    }
}
