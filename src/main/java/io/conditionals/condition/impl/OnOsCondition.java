package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnOs;
import io.conditionals.condition.utils.ConditionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Locale;

public class OnOsCondition extends MatchingSpringBootCondition {
    public static final String OS_NAME_PROPERTY_KEY = "os.name";
    private static final String VALUE = "value";

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return ConditionalOnOs.class;
    }

    @Override
    protected ConditionOutcome determineOutcome(ConditionMessage.Builder message, ConditionContext context, AnnotationAttributes annotationAttributes) {
        String osName = this.getOsName(context);
        String[] values = annotationAttributes.getStringArray(VALUE);
        boolean matched = this.isOsIncluded(values, osName);
        return matched
                ? ConditionOutcome.match(message.found("OS").items(osName))
                : ConditionUtils.noMatchBecause(message, "OS '", osName, "' did not match any of ", Arrays.toString(values));
    }

    private String getOsName(ConditionContext context) {
        return context.getEnvironment()
                .getProperty(OS_NAME_PROPERTY_KEY, System.getProperty(OS_NAME_PROPERTY_KEY, ""))
                .toLowerCase(Locale.ROOT);
    }

    private boolean isOsIncluded(String[] values, String osName) {
        return Arrays.stream(values)
                .map(value -> value.toLowerCase(Locale.ROOT))
                .anyMatch(osName::contains);
    }
}
