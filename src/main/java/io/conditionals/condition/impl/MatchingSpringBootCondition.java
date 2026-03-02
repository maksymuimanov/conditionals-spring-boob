package io.conditionals.condition.impl;

import io.conditionals.condition.utils.ConditionUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class MatchingSpringBootCondition extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        List<ConditionMessage> noMatch = new ArrayList<>();
        List<ConditionMessage> match = new ArrayList<>();

        this.getAttributesStream(metadata)
                .forEach(annotation -> {
                    ConditionOutcome outcome = this.checkAttributes(this.getParentMessage(), context, annotation);
                    (outcome.isMatch() ? match : noMatch).add(outcome.getConditionMessage());
                });

        return noMatch.isEmpty()
                ? ConditionOutcome.match(ConditionMessage.of(match))
                : ConditionOutcome.noMatch(ConditionMessage.of(noMatch));
    }

    protected Stream<@Nullable AnnotationAttributes> getAttributesStream(AnnotatedTypeMetadata metadata) {
        return Stream.of(ConditionUtils.attributes(metadata, this.getAnnotationClass()));
    }

    /**
     * Validates that attributes are present and delegates evaluation to a caller-provided function.
     *
     * <p><b>Semantics</b></p>
     * <ul>
     *     <li>If {@code attributes} is {@code null}, returns {@link ConditionUtils#noAttributesFound(ConditionMessage.Builder)}.</li>
     *     <li>Otherwise, invokes {@code outcomeFunction} with the non-null attributes.</li>
     * </ul>
     *
     * <p><b>Null Handling</b></p>
     * <p>{@code attributes} may be {@code null} and is treated as absent.</p>
     *
     * @param message message builder used to construct the no-attributes message
     * @param attributes annotation attributes, or {@code null}
     * @return outcome based on presence and evaluation of attributes
     */
    protected ConditionOutcome checkAttributes(ConditionMessage.Builder message, ConditionContext context, @Nullable AnnotationAttributes attributes) {
        boolean empty = attributes == null;
        return empty
                ? ConditionUtils.noAttributesFound(message)
                : this.determineOutcome(message, context, attributes);
    }

    protected ConditionMessage.Builder getParentMessage() {
        return ConditionMessage.forCondition(this.getAnnotationClass());
    }

    protected abstract Class<? extends Annotation> getAnnotationClass();

    protected abstract ConditionOutcome determineOutcome(ConditionMessage.Builder message, ConditionContext context, AnnotationAttributes annotationAttributes);
}