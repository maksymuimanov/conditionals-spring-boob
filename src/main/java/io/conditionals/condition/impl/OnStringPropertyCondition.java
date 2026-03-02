package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnStringProperties;
import io.conditionals.condition.ConditionalOnStringProperty;
import io.conditionals.condition.spec.MatchingPropertySpec;
import io.conditionals.condition.spec.PropertySpecMatcher;
import io.conditionals.condition.spec.StringMatchType;
import io.conditionals.condition.utils.ConditionUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.annotation.Annotation;
import java.util.Locale;

public class OnStringPropertyCondition extends PropertySpringBootCondition<String, OnStringPropertyCondition.Spec> {
    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return ConditionalOnStringProperty.class;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationContainerClass() {
        return ConditionalOnStringProperties.class;
    }

    @Override
    protected Spec createSpec(@Nullable Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
        return new Spec(annotationType, annotationAttributes);
    }

    @Override
    protected PropertySpecMatcher<String, Spec> createPropertySpecMatcher() {
        return new Matcher();
    }

    public class Matcher implements PropertySpecMatcher<String, Spec> {
        @Override
        public boolean compare(Spec spec, @Nullable String property, String candidate) {
            if (property == null) return false;
            if (spec.ignoreCase) {
                property = property.toLowerCase(Locale.ROOT);
                candidate = candidate.toLowerCase(Locale.ROOT);
            }
            if (spec.trim) {
                property = property.trim();
                candidate = candidate.trim();
            }
            boolean result = switch (spec.getMatchType()) {
                case EQUALS -> property.equals(candidate);
                case CONTAINS -> property.contains(candidate);
                case STARTS_WITH -> property.startsWith(candidate);
                case ENDS_WITH -> property.endsWith(candidate);
                case MATCHES -> property.matches(candidate);
            };
            return ConditionUtils.revert(result, spec.isNot());
        }
    }

    public class Spec extends MatchingPropertySpec<String, Spec, StringMatchType> {
        private static final String IGNORE_CASE = "ignoreCase";
        private static final String TRIM = "trim";
        private final boolean ignoreCase;
        private final boolean trim;

        private Spec(Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
            super(annotationType, annotationAttributes);
            this.ignoreCase = annotationAttributes.getBoolean(IGNORE_CASE);
            this.trim = annotationAttributes.getBoolean(TRIM);
        }
    }
}
