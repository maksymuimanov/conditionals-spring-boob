package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnCollectionProperties;
import io.conditionals.condition.ConditionalOnCollectionProperty;
import io.conditionals.condition.dto.MatchingPropertySpec;
import io.conditionals.condition.utils.ConditionUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.stream.Stream;

public class OnCollectionPropertyCondition extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context,
                                            AnnotatedTypeMetadata metadata) {
        ConditionMessage.Builder message = ConditionMessage.forCondition(ConditionalOnCollectionProperty.class);
        Stream<@Nullable AnnotationAttributes> annotationAttributes = ConditionUtils.mergedStream(metadata, ConditionalOnCollectionProperty.class, ConditionalOnCollectionProperties.class);
        return ConditionUtils.evaluateConditions(message, annotationAttributes, attributes ->
                ConditionUtils.evaluatePropertyConditions(message, attributes, Spec::new, context.getEnvironment(), (spec, property, candidate) -> {
                    if (property == null) return false;
                    if (spec.size != -1 && property.length != spec.size) return spec.isNot();
                    boolean result = switch (spec.getMatchType()) {
                        case EQUALS -> Arrays.equals(property, candidate);
                        case CONTAINS_ALL -> containsSubArray(property, candidate);
                        case CONTAINS_SEQUENCE -> containsSequence(property, candidate);
                        case STARTS_WITH -> startsWithSubArray(property, candidate);
                        case ENDS_WITH -> endsWithSubArray(property, candidate);
                    };
                    return result ^ spec.isNot();
                })
        );
    }

    private static boolean containsSubArray(String[] property, String[] candidate) {
        int candidateLength = candidate.length;
        if (candidateLength == 0) return true;
        int propertyLength = property.length;
        if (propertyLength < candidateLength) return false;

        for (String candidateElement : candidate) {
            boolean found = false;
            for (String propertyElement : property) {
                if (propertyElement.equals(candidateElement)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }

    private static boolean containsSequence(String[] property, String[] candidate) {
        int candidateLength = candidate.length;
        if (candidateLength == 0) return true;
        int propertyLength = property.length;
        if (propertyLength < candidateLength) return false;

        outer:
        for (int i = 0; i <= property.length - candidate.length; i++) {
            for (int j = 0; j < candidate.length; j++) {
                if (!property[i + j].equals(candidate[j])) {
                    continue outer;
                }
            }
            return true;
        }
        return false;
    }

    private static boolean startsWithSubArray(String[] property, String[] candidate) {
        int candidateLength = candidate.length;
        if (candidateLength == 0) return true;
        int propertyLength = property.length;
        if (propertyLength < candidateLength) return false;

        for (int i = 0; i < candidateLength; i++) {
            if (!property[i].equals(candidate[i])) {
                return false;
            }
        }

        return true;
    }

    private static boolean endsWithSubArray(String[] property, String[] candidate) {
        int candidateLength = candidate.length;
        if (candidateLength == 0) return true;
        int propertyLength = property.length;
        if (propertyLength < candidateLength) return false;

        for (int i = 0; i < candidateLength; i++) {
            if (!property[propertyLength - candidateLength + i].equals(candidate[i])) {
                return false;
            }
        }

        return true;
    }

    private static class Spec extends MatchingPropertySpec<String[], Spec, ConditionalOnCollectionProperty.MatchType> {
        private static final String SIZE = "size";
        private final int size;

        private Spec(Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
            super(annotationType, annotationAttributes);
            this.size = annotationAttributes.getNumber(SIZE).intValue();
        }
    }
}
