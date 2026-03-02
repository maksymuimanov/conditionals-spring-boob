package io.conditionals.condition.impl;

import io.conditionals.condition.ConditionalOnMapProperties;
import io.conditionals.condition.ConditionalOnMapProperty;
import io.conditionals.condition.spec.MapMatchType;
import io.conditionals.condition.spec.MatchingPropertySpec;
import io.conditionals.condition.spec.PropertySpecMatcher;
import io.conditionals.condition.utils.ConditionUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.env.PropertyResolver;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnMapPropertyCondition extends PropertySpringBootCondition<Map<String, String>, OnMapPropertyCondition.Spec> {
    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return ConditionalOnMapProperty.class;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationContainerClass() {
        return ConditionalOnMapProperties.class;
    }

    @Override
    protected Spec createSpec(@Nullable Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
        return new Spec(annotationType, annotationAttributes);
    }

    @Override
    protected PropertySpecMatcher<Map<String, String>, Spec> createPropertySpecMatcher() {
        return new Matcher();
    }

    public class Matcher implements PropertySpecMatcher<Map<String, String>, Spec> {
        @Override
        public boolean compare(Spec spec, @Nullable Map<String, String> property, Map<String, String> candidate) {
            if (property == null) return false;
            boolean result = switch (spec.getMatchType()) {
                case EQUALS -> equals(property, candidate);
                case CONTAINS_ANY -> containsAny(property, candidate);
                case CONTAINS_ALL -> containsAll(property, candidate);
            };
            return ConditionUtils.revert(result, spec.isNot());
        }

        private static boolean equals(Map<String, String> property, Map<String, String> candidate) {
            int candidateSize = candidate.size();
            if (candidateSize == 0) return true;

            return Map.copyOf(property).equals(Map.copyOf(candidate));
        }

        private static boolean containsAny(Map<String, String> property, Map<String, String> candidate) {
            int candidateSize = candidate.size();
            if (candidateSize == 0) return true;

            Iterable<Map.Entry<String, String>> entrySet = candidate.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                if (property.containsKey(entry.getKey()) && property.get(entry.getKey()).equals(entry.getValue())) {
                    return true;
                }
            }

            return false;
        }

        private static boolean containsAll(Map<String, String> property, Map<String, String> candidate) {
            int candidateSize = candidate.size();
            if (candidateSize == 0) return true;
            int propertySize = property.size();
            if (propertySize < candidateSize) return false;

            for (Map.Entry<String, String> entry : candidate.entrySet()) {
                if (!property.containsKey(entry.getKey()) || !property.get(entry.getKey()).equals(entry.getValue())) {
                    return false;
                }
            }

            return true;
        }
    }

    public class Spec extends MatchingPropertySpec<Map<String, String>, Spec, MapMatchType> {
        private Spec(Class<? extends Annotation> annotationType, AnnotationAttributes annotationAttributes) {
            super(annotationType, annotationAttributes, attribute -> {
                String[] pairs = (String[]) attribute;
                if (pairs.length % 2 != 0) throw new IllegalArgumentException("Map must have even number of elements");
                Map<String, String> map = new HashMap<>();
                for (int i = 0; i < pairs.length; i += 2) {
                    map.put(pairs[i], pairs[i + 1]);
                }
                return map;
            });
        }

        @Override
        public void collectProperties(PropertyResolver resolver, List<String> missing, List<String> nonMatching, PropertySpecMatcher<Map<String, String>, Spec> matcher) {
            for (String name : this.getNames()) {
                try {
                    Map<String, String> propertyMap = new HashMap<>();
                    Iterable<String> mapKeys = this.getHavingValue().keySet();
                    for (String mapKey : mapKeys) {
                        String key = this.getPrefix() + name + "." + mapKey;
                        if (resolver.containsProperty(key)) {
                            String property = resolver.getProperty(key, String.class);
                            propertyMap.put(mapKey, property);
                        } else if (!this.isMatchIfMissing()) {
                            missing.add(name);
                        }
                    }

                    if (!this.isMatch(propertyMap, matcher) && !this.isMatchIfMissing()) {
                        nonMatching.add(name);
                    }
                } catch (ConversionException e) {
                    nonMatching.add(name);
                }
            }
        }
    }
}
