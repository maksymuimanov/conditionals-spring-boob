package io.conditionals.condition.impl;

import io.conditionals.condition.spec.PropertySpec;
import io.conditionals.condition.spec.PropertySpecMatcher;
import io.conditionals.condition.utils.ConditionUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class PropertySpringBootCondition<V, S extends PropertySpec<V, S>> extends MatchingSpringBootCondition {
    @Override
    protected Stream<@Nullable AnnotationAttributes> getAttributesStream(AnnotatedTypeMetadata metadata) {
        return ConditionUtils.mergedStream(metadata, this.getAnnotationClass(), this.getAnnotationContainerClass());
    }

    protected abstract Class<? extends Annotation> getAnnotationContainerClass();

    @Override
    protected ConditionOutcome determineOutcome(ConditionMessage.Builder message, ConditionContext context, AnnotationAttributes annotationAttributes) {
        Class<? extends Annotation> annotationType = annotationAttributes.annotationType();
        S spec = this.createSpec(annotationType, annotationAttributes);
        List<String> missingProperties = new ArrayList<>();
        List<String> nonMatchingProperties = new ArrayList<>();
        PropertyResolver resolver = context.getEnvironment();
        spec.collectProperties(resolver, missingProperties, nonMatchingProperties, this.createPropertySpecMatcher());
        if (!missingProperties.isEmpty()) {
            return ConditionOutcome.noMatch(message.didNotFind("property", "properties")
                    .items(ConditionMessage.Style.QUOTE, missingProperties));
        } else {
            return !nonMatchingProperties.isEmpty()
                    ? ConditionOutcome.noMatch(message.found("different value in property", "different value in properties")
                            .items(ConditionMessage.Style.QUOTE, nonMatchingProperties))
                    : ConditionOutcome.match(message.because("matched"));
        }
    }

    protected abstract S createSpec(@Nullable Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes);

    protected abstract PropertySpecMatcher<V, S> createPropertySpecMatcher();
}