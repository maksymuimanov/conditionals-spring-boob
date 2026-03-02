package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnCollectionProperties;
import io.conditionals.condition.ConditionalOnCollectionProperty;
import io.conditionals.condition.spec.CollectionMatchType;
import io.conditionals.condition.spec.MatchingPropertySpec;
import io.conditionals.condition.spec.PropertySpecMatcher;
import io.conditionals.condition.utils.ConditionUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class OnCollectionPropertyCondition extends PropertySpringBootCondition<String[], OnCollectionPropertyCondition.Spec> {
    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return ConditionalOnCollectionProperty.class;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationContainerClass() {
        return ConditionalOnCollectionProperties.class;
    }

    @Override
    protected Spec createSpec(@Nullable Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
        return new Spec(annotationType, annotationAttributes);
    }

    @Override
    protected PropertySpecMatcher<String[], Spec> createPropertySpecMatcher() {
        return new Matcher();
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    public class Matcher implements PropertySpecMatcher<String[], Spec> {
        @Override
        public boolean compare(Spec spec, String @Nullable[] property, String[] candidate) {
            if (property == null) return false;
            if (spec.size != -1 && property.length != spec.size) return spec.isNot();
            boolean result = switch (spec.getMatchType()) {
                case EQUALS -> equals(property, candidate);
                case CONTAINS_ANY -> containsAny(property, candidate);
                case CONTAINS_ALL -> containsAll(property, candidate);
                case CONTAINS_SEQUENCE -> containsSequence(property, candidate);
                case STARTS_WITH_ANY -> startsWithAny(property, candidate);
                case STARTS_WITH_ALL -> startsWithAll(property, candidate);
                case ENDS_WITH_ANY -> endsWithAny(property, candidate);
                case ENDS_WITH_ALL -> endsWithAll(property, candidate);
            };
            return ConditionUtils.revert(result, spec.isNot());
        }

        private static boolean equals(String[] property, String[] candidate) {
            return Arrays.equals(property, candidate);
        }

        private static boolean containsAny(String[] property, String[] candidate) {
            int candidateLength = candidate.length;
            if (candidateLength == 0) return true;

            for (String candidateElement : candidate) {
                for (String propertyElement : property) {
                    if (propertyElement.equals(candidateElement)) {
                        return true;
                    }
                }
            }

            return false;
        }

        private static boolean containsAll(String[] property, String[] candidate) {
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

        private static boolean startsWithAny(String[] property, String[] candidate) {
            int candidateLength = candidate.length;
            if (candidateLength == 0) return true;

            for (String candidateElement : candidate) {
                if (property[0].equals(candidateElement)) {
                    return true;
                }
            }

            return false;
        }

        private static boolean startsWithAll(String[] property, String[] candidate) {
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

        private static boolean endsWithAny(String[] property, String[] candidate) {
            int candidateLength = candidate.length;
            if (candidateLength == 0) return true;
            int propertyLength = property.length;

            for (String candidateElement : candidate) {
                if (property[propertyLength - 1].equals(candidateElement)) {
                    return true;
                }
            }

            return false;
        }

        private static boolean endsWithAll(String[] property, String[] candidate) {
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
    }

    public class Spec extends MatchingPropertySpec<String[], Spec, CollectionMatchType> {
        private static final String SIZE = "size";
        private final int size;

        private Spec(Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
            super(annotationType, annotationAttributes);
            this.size = annotationAttributes.getNumber(SIZE).intValue();
        }
    }
}
