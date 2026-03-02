package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnCharacterProperties;
import io.conditionals.condition.ConditionalOnCharacterProperty;

import java.lang.annotation.Annotation;

public class OnCharacterPropertyCondition extends ComparablePropertySpringBootCondition<Character, Character> {
    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return ConditionalOnCharacterProperty.class;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationContainerClass() {
        return ConditionalOnCharacterProperties.class;
    }
}
