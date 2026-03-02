package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnCharacterProperties;
import io.conditionals.condition.ConditionalOnCharacterProperty;
import io.conditionals.condition.dto.MatchingPropertySpec;
import io.conditionals.condition.dto.NumericMatchType;
import io.conditionals.condition.dto.PropertySpecMatcher;
import io.conditionals.condition.utils.ConditionUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.annotation.Annotation;

public class OnCharacterPropertyCondition extends PropertySpringBootCondition<Character, OnCharacterPropertyCondition.Spec> {
    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return ConditionalOnCharacterProperty.class;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationContainerClass() {
        return ConditionalOnCharacterProperties.class;
    }

    @Override
    protected Spec createSpec(@Nullable Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
        return new Spec(annotationType, annotationAttributes);
    }

    @Override
    protected PropertySpecMatcher<Character, Spec> createPropertySpecMatcher() {
        return ((spec, property, candidate) -> {
            if (property == null) return false;
            boolean result = switch (spec.getMatchType()) {
                case EQUALS -> property.equals(candidate);
                case GREATER_THAN -> property.compareTo(candidate) > 0;
                case LESS_THAN -> property.compareTo(candidate) < 0;
                case GREATER_THAN_OR_EQUAL -> property.compareTo(candidate) >= 0;
                case LESS_THAN_OR_EQUAL -> property.compareTo(candidate) <= 0;
            };
            return ConditionUtils.revert(result, spec.isNot());
        });
    }


    public static class Spec extends MatchingPropertySpec<Character, Spec, NumericMatchType> {
        private Spec(Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
            super(annotationType, annotationAttributes);
        }
    }
}
