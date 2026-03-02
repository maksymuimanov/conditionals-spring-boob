package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnDurationProperties;
import io.conditionals.condition.ConditionalOnDurationProperty;
import io.conditionals.condition.spec.ComparablePropertySpec;
import io.conditionals.condition.spec.PropertySpecMatcher;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.env.PropertyResolver;

import java.lang.annotation.Annotation;
import java.time.Duration;
import java.util.List;

public class OnDurationPropertyCondition extends ComparablePropertySpringBootCondition<Duration, Duration> {
    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return ConditionalOnDurationProperty.class;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationContainerClass() {
        return ConditionalOnDurationProperties.class;
    }

    @Override
    protected ComparablePropertySpec<Duration, Duration> createSpec(@Nullable Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
        return new Spec(annotationType, annotationAttributes);
    }

    public class Spec extends ComparablePropertySpec<Duration, Duration> {
        public Spec(Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
            super(annotationType, annotationAttributes, attribute -> DurationStyle.detectAndParse(String.valueOf(attribute)));
        }

        @Override
        public void collectProperties(PropertyResolver resolver, List<String> missing, List<String> nonMatching, PropertySpecMatcher<Duration, ComparablePropertySpec<Duration, Duration>> matcher) {
            for (String name : this.getNames()) {
                try {
                    String key = this.getPrefix() + name;
                    if (resolver.containsProperty(key)) {
                        String property = resolver.getProperty(key);
                        Duration duration = DurationStyle.detectAndParse(property);
                        if (!this.isMatch(duration, matcher)) {
                            nonMatching.add(name);
                        }
                    } else if (!this.isMatchIfMissing()) {
                        missing.add(name);
                    }
                } catch (ConversionException e) {
                    nonMatching.add(name);
                }
            }
        }
    }
}
