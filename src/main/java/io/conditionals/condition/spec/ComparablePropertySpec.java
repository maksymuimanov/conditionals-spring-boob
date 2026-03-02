package io.conditionals.condition.spec;

import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.annotation.Annotation;
import java.util.function.Function;

public class ComparablePropertySpec<V extends Comparable<T>, T> extends MatchingPropertySpec<V, ComparablePropertySpec<V, T>, ComparableMatchType> {
    public ComparablePropertySpec(Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
        super(annotationType, annotationAttributes);
    }

    public ComparablePropertySpec(Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes, Function<Object, V> havingValueMapper) {
        super(annotationType, annotationAttributes, havingValueMapper);
    }
}
