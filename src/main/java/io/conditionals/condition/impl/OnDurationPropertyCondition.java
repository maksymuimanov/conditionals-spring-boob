package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnDurationProperties;
import io.conditionals.condition.ConditionalOnDurationProperty;
import io.conditionals.condition.dto.ComparablePropertySpec;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.annotation.Annotation;
import java.time.Duration;

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
        return new ComparablePropertySpec<>(annotationType, annotationAttributes, attribute -> DurationStyle.detectAndParse(String.valueOf(attribute)));
    }
}
