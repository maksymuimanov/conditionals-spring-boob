package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnDurationProperties;
import io.conditionals.condition.ConditionalOnDurationProperty;
import io.conditionals.condition.dto.MatchingPropertySpec;
import io.conditionals.condition.dto.NumericMatchType;
import io.conditionals.condition.dto.PropertySpecMatcher;
import io.conditionals.condition.utils.ConditionUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.annotation.Annotation;
import java.time.Duration;

public class OnDurationPropertyCondition extends PropertySpringBootCondition<Duration, OnDurationPropertyCondition.Spec> {
    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return ConditionalOnDurationProperty.class;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationContainerClass() {
        return ConditionalOnDurationProperties.class;
    }

    @Override
    protected Spec createSpec(@Nullable Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
        return new Spec(annotationType, annotationAttributes);
    }

    @Override
    protected PropertySpecMatcher<Duration, Spec> createPropertySpecMatcher() {
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


    public static class Spec extends MatchingPropertySpec<Duration, Spec, NumericMatchType> {
        private Spec(Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
            super(annotationType, annotationAttributes, attribute -> DurationStyle.detectAndParse(String.valueOf(attribute)));
        }
    }
}
