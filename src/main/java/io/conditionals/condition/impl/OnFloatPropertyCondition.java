package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnFloatProperties;
import io.conditionals.condition.ConditionalOnFloatProperty;

import java.lang.annotation.Annotation;

public class OnFloatPropertyCondition extends ComparablePropertySpringBootCondition<Float, Float> {
    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return ConditionalOnFloatProperty.class;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationContainerClass() {
        return ConditionalOnFloatProperties.class;
    }
}
