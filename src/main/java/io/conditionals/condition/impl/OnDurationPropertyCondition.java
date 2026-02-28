package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnDurationProperties;
import io.conditionals.condition.ConditionalOnDurationProperty;
import io.conditionals.condition.dto.MatchingPropertySpec;
import io.conditionals.condition.utils.ConditionUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.Annotation;
import java.time.Duration;
import java.util.stream.Stream;

public class OnDurationPropertyCondition extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context,
                                            AnnotatedTypeMetadata metadata) {
        ConditionMessage.Builder message = ConditionMessage.forCondition(ConditionalOnDurationProperty.class);
        Stream<@Nullable AnnotationAttributes> annotationAttributes = ConditionUtils.mergedStream(metadata, ConditionalOnDurationProperty.class, ConditionalOnDurationProperties.class);
        return ConditionUtils.evaluateConditions(message, annotationAttributes, attributes ->
                ConditionUtils.evaluatePropertyConditions(message, attributes, Spec::new, context.getEnvironment(), (spec, property, candidate) -> {
                    if (property == null) return false;
                    boolean result = switch (spec.getMatchType()) {
                        case EQUALS -> property.equals(candidate);
                        case GREATER_THAN -> property.compareTo(candidate) > 0;
                        case LESS_THAN -> property.compareTo(candidate) < 0;
                        case GREATER_THAN_OR_EQUAL -> property.compareTo(candidate) >= 0;
                        case LESS_THAN_OR_EQUAL -> property.compareTo(candidate) <= 0;
                    };
                    return result ^ spec.isNot();
                })
        );
    }

    private static class Spec extends MatchingPropertySpec<Duration, Spec, ConditionalOnDurationProperty.MatchType> {
        private Spec(Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
            super(annotationType, annotationAttributes, attribute -> DurationStyle.detectAndParse(String.valueOf(attribute)));
        }
    }
}
