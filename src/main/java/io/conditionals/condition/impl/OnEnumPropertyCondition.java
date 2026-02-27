package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnEnumProperties;
import io.conditionals.condition.ConditionalOnEnumProperty;
import io.conditionals.condition.ConditionalOnOs;
import io.conditionals.condition.dto.PropertySpec;
import io.conditionals.condition.utils.ConditionUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Stream;

public class OnEnumPropertyCondition extends SpringBootCondition {
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public ConditionOutcome getMatchOutcome(ConditionContext context,
                                            AnnotatedTypeMetadata metadata) {
        ConditionMessage.Builder message = ConditionMessage.forCondition(ConditionalOnEnumProperty.class);
        Stream<@Nullable AnnotationAttributes> annotationAttributes = ConditionUtils.mergedStream(metadata, ConditionalOnEnumProperty.class, ConditionalOnEnumProperties.class);
        return ConditionUtils.evaluateConditions(message, annotationAttributes, attributes ->
                ConditionUtils.evaluateConditions(message, attributes, Spec::new, context.getEnvironment(), (spec, v1, v2) -> {
                            Class<? extends Enum> enumClass = spec.enumClass;
                            String v1Name = v1.toUpperCase(Locale.ROOT);
                            Enum v1Enum = Enum.valueOf(enumClass, v1Name);
                            String v2Name = v2.toUpperCase(Locale.ROOT);
                            Enum v2Enum = Enum.valueOf(enumClass, v2Name);
                            return v1Enum.equals(v2Enum);
                        }
                ));
    }

    @SuppressWarnings("rawtypes")
    private static class Spec extends PropertySpec<String, Spec> {
        private static final String ENUM_CLASS = "enumClass";
        private final Class<? extends Enum> enumClass;

        protected Spec(Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
            super(annotationType, annotationAttributes);
            this.enumClass = annotationAttributes.getClass(ENUM_CLASS);
        }
    }
}
