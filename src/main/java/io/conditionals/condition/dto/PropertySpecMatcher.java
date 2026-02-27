package io.conditionals.condition.dto;

import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface PropertySpecMatcher<V, S extends PropertySpec<V, S>> {
    boolean compare(S spec, @Nullable V property, V candidate);
}
