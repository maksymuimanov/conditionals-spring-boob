package io.conditionals.condition.utils;

import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public final class ConditionUtils {
    private static final String VALUE = "value";

    private ConditionUtils() {
    }

    public static Stream<@Nullable AnnotationAttributes> mergedStream(AnnotatedTypeMetadata metadata,
                                                                      Class<? extends Annotation> annotationClass,
                                                                      Class<? extends Annotation> containerAnnotationClass) {
        AnnotationAttributes spec = attributes(metadata, annotationClass);
        Stream<@Nullable AnnotationAttributes> stream = stream(metadata, containerAnnotationClass);
        return spec == null
                ? stream
                : Stream.concat(Stream.of(spec), stream);
    }

    public static Stream<@Nullable AnnotationAttributes> stream(AnnotatedTypeMetadata metadata,
                                                                Class<? extends Annotation> annotationClass) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(annotationClass.getName());
        if (attributes == null)
            return Stream.of();
        AnnotationAttributes[] annotationAttributes = (AnnotationAttributes[]) attributes.get(VALUE);
        return Arrays.stream(annotationAttributes);
    }

    @Nullable
    public static AnnotationAttributes attributes(AnnotatedTypeMetadata metadata,
                                                  Class<? extends Annotation> annotationClass) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(annotationClass.getName());
        return AnnotationAttributes.fromMap(attributes);
    }

    public static ConditionOutcome match(String... message) {
        return ConditionOutcome.match(String.join("", message));
    }

    public static ConditionOutcome noAttributesFound(ConditionMessage.Builder message) {
        return noMatchBecause(message, "no annotation attributes were found");
    }

    public static ConditionOutcome noMatchBecause(ConditionMessage.Builder message, String... reason) {
        return ConditionOutcome.noMatch(message.because(String.join("", reason)));
    }

    public static ConditionOutcome noMatch(String... message) {
        return ConditionOutcome.noMatch(String.join("", message));
    }

    public static boolean revert(boolean condition, boolean revert) {
        return condition ^ revert;
    }
}
