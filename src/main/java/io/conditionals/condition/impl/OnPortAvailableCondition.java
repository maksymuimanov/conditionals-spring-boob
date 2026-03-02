package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnPortAvailable;
import io.conditionals.condition.utils.ConditionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.ServerSocket;
import java.util.Arrays;

public class OnPortAvailableCondition extends MatchingSpringBootCondition {
    private static final String VALUE = "value";

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return ConditionalOnPortAvailable.class;
    }

    @Override
    protected ConditionOutcome determineOutcome(ConditionMessage.Builder message, ConditionContext context, AnnotationAttributes annotationAttributes) {
        int[] ports = (int[]) annotationAttributes.get(VALUE);
        boolean portAvailable = isPortAvailable(ports);
        return portAvailable
                ? ConditionOutcome.match()
                : ConditionUtils.noMatchBecause(message, "Port ", Arrays.toString(ports), " is not available");
    }

    private boolean isPortAvailable(int[] ports) {
        for (int port : ports) {
            try (ServerSocket socket = new ServerSocket(port)) {
                socket.setReuseAddress(true);
            } catch (IOException e) {
                return false;
            }
        }

        return true;
    }
}
