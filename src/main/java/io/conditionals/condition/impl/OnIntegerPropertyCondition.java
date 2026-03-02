package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnIntegerProperties;
import io.conditionals.condition.ConditionalOnIntegerProperty;

import java.lang.annotation.Annotation;

public class OnIntegerPropertyCondition extends ComparablePropertySpringBootCondition<Integer, Integer> {
    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return ConditionalOnIntegerProperty.class;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationContainerClass() {
        return ConditionalOnIntegerProperties.class;
    }
}
