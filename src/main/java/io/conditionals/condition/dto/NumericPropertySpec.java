package io.conditionals.condition.dto;

import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.annotation.Annotation;
import java.util.function.Function;

public class NumericPropertySpec<V extends Number> extends MatchingPropertySpec<V, NumericPropertySpec<V>, NumericMatchType> {
    public NumericPropertySpec(Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
        super(annotationType, annotationAttributes);
    }

    public NumericPropertySpec(Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes, Function<Object, V> havingValueMapper) {
        super(annotationType, annotationAttributes, havingValueMapper);
    }
}
