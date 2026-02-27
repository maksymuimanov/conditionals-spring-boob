package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnOs;
import io.conditionals.condition.utils.ConditionUtils;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;
import java.util.Locale;

public class OnOsCondition extends SpringBootCondition {
    public static final String OS_NAME_PROPERTY_KEY = "os.name";

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context,
                                            AnnotatedTypeMetadata metadata) {
        ConditionMessage.Builder message = ConditionMessage.forCondition(ConditionalOnOs.class);
        AnnotationAttributes annotationAttributes = ConditionUtils.attributes(metadata, ConditionalOnOs.class);
        return ConditionUtils.checkAttributes(message, annotationAttributes, (attributes) -> {
            String osName = context.getEnvironment()
                    .getProperty(OS_NAME_PROPERTY_KEY, System.getProperty(OS_NAME_PROPERTY_KEY, ""))
                    .toLowerCase(Locale.ROOT);
            String[] values = attributes.getStringArray("value");
            boolean matched = Arrays.stream(values)
                    .map(value -> value.toLowerCase(Locale.ROOT))
                    .anyMatch(osName::contains);
            return matched
                    ? ConditionOutcome.match(message.found("OS").items(osName))
                    : ConditionUtils.noMatchBecause(message, "OS '", osName, "' did not match any of ", Arrays.toString(values));
        });
    }
}
