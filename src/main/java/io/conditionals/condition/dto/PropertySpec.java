package io.conditionals.condition.dto;

import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.PropertyResolver;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.List;

public class PropertySpec<V, S extends PropertySpec<V, S>> {
    protected static final String VALUE = "value";
    protected static final String PREFIX = "prefix";
    protected static final String NAME = "name";
    protected static final String HAVING_VALUE = "havingValue";
    protected static final String MATCH_IF_MISSING = "matchIfMissing";
    private final Class<? extends Annotation> annotationType;
    private final String prefix;
    private final String[] names;
    private final V havingValue;
    private final Class<V> havingValueType;
    private final boolean matchIfMissing;

    @SuppressWarnings("unchecked")
    protected PropertySpec(Class<? extends Annotation> annotationType,
                           AnnotationAttributes annotationAttributes) {
        this.annotationType = annotationType;
        this.prefix = !annotationAttributes.containsKey(PREFIX)
                ? ""
                : this.getPrefix(annotationAttributes);
        this.names = this.getNames(annotationAttributes);
        this.havingValue = (V) annotationAttributes.get(HAVING_VALUE);
        this.havingValueType = (Class<V>) annotationAttributes.getClass(HAVING_VALUE);
        this.matchIfMissing = annotationAttributes.getBoolean(MATCH_IF_MISSING);
    }

    protected String getPrefix(AnnotationAttributes annotationAttributes) {
        String prefix = annotationAttributes.getString(PREFIX).trim();
        if (StringUtils.hasText(prefix) && !prefix.endsWith(".")) {
            prefix = prefix + ".";
        }

        return prefix;
    }

    protected String[] getNames(AnnotationAttributes annotationAttributes) {
        String[] value = (String[]) annotationAttributes.get(VALUE);
        String[] name = (String[]) annotationAttributes.get(NAME);
        Assert.state(value.length > 0 || name.length > 0,
                () -> "The name or value attribute of @%s must be specified".formatted(ClassUtils.getShortName(this.annotationType)));
        Assert.state(value.length == 0 || name.length == 0,
                () -> "The name and value attributes of @%s are exclusive".formatted(ClassUtils.getShortName(this.annotationType)));
        return value.length > 0
                ? value
                : name;
    }

    public void collectProperties(PropertyResolver resolver,
                                  List<String> missing,
                                  List<String> nonMatching,
                                  PropertySpecMatcher<V, S> matcher) {
        for(String name : this.names) {
            String key = this.prefix + name;
            if (resolver.containsProperty(key)) {
                if (!this.isMatch(resolver.getProperty(key, this.havingValueType), matcher)) {
                    nonMatching.add(name);
                }
            } else if (!this.matchIfMissing) {
                missing.add(name);
            }
        }

    }

    @SuppressWarnings("unchecked")
    protected boolean isMatch(V value, PropertySpecMatcher<V, S> matcher) {
        return matcher.compare((S) this, value, this.havingValue);
    }

    public String toString() {
        StringBuilder result = new StringBuilder()
                .append("(")
                .append(this.prefix);
        if (this.names.length == 1) {
            result.append(this.names[0]);
        } else {
            result.append("[")
                    .append(StringUtils.arrayToCommaDelimitedString(this.names))
                    .append("]");
        }
        return result.append("=")
                .append(this.havingValue)
                .append(")")
                .toString();
    }
}
