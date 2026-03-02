package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnLongProperties;
import io.conditionals.condition.ConditionalOnLongProperty;

import java.lang.annotation.Annotation;

public class OnLongPropertyCondition extends ComparablePropertySpringBootCondition<Long, Long> {
    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return ConditionalOnLongProperty.class;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationContainerClass() {
        return ConditionalOnLongProperties.class;
    }
}
