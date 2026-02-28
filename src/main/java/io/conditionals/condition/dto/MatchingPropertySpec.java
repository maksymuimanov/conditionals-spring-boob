package io.conditionals.condition.dto;

import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.annotation.Annotation;
import java.util.function.Function;

public abstract class MatchingPropertySpec<V, S extends MatchingPropertySpec<V, S, E>, E extends Enum<E>> extends PropertySpec<V, S> {
    private static final String NOT = "not";
    private static final String MATCH_TYPE = "matchType";
    private final boolean not;
    private final E matchType;

    protected MatchingPropertySpec(Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
        super(annotationType, annotationAttributes);
        this.not = annotationAttributes.getBoolean(NOT);
        this.matchType = annotationAttributes.getEnum(MATCH_TYPE);
    }

    protected MatchingPropertySpec(Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes, Function<Object, V> havingValueMapper) {
        super(annotationType, annotationAttributes, havingValueMapper);
        this.not = annotationAttributes.getBoolean(NOT);
        this.matchType = annotationAttributes.getEnum(MATCH_TYPE);
    }

    public boolean isNot() {
        return not;
    }

    public E getMatchType() {
        return matchType;
    }
}
