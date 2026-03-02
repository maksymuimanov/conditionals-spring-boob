package io.conditionals.condition.spec;

import org.jspecify.annotations.Nullable;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.env.PropertyResolver;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.Function;

public abstract class PropertySpec<V, S extends PropertySpec<V, S>> {
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
        this(annotationType, annotationAttributes, attribute -> (V) attribute);
    }

    @SuppressWarnings("unchecked")
    protected PropertySpec(Class<? extends Annotation> annotationType,
                           AnnotationAttributes annotationAttributes,
                           Function<Object, V> havingValueMapper) {
        this.annotationType = annotationType;
        this.prefix = this.resolvePrefix(annotationAttributes);
        this.names = this.resolveNames(annotationAttributes);
        this.havingValue = havingValueMapper.apply(annotationAttributes.get(HAVING_VALUE));
        this.havingValueType = (Class<V>) this.havingValue.getClass();
        this.matchIfMissing = annotationAttributes.getBoolean(MATCH_IF_MISSING);
    }

    protected String resolvePrefix(AnnotationAttributes annotationAttributes) {
        if (!annotationAttributes.containsKey(PREFIX)) return "";
        String prefix = annotationAttributes.getString(PREFIX).trim();
        if (StringUtils.hasText(prefix) && !prefix.endsWith(".")) {
            prefix = prefix + ".";
        }

        return prefix;
    }

    protected String[] resolveNames(AnnotationAttributes annotationAttributes) {
        String[] value = (String[]) annotationAttributes.get(VALUE);
        String[] name = (String[]) annotationAttributes.get(NAME);
        Assert.state(value.length > 0 || name.length > 0,
                () -> "The name or value attribute of @%s must be specified".formatted(ClassUtils.getShortName(this.getAnnotationType())));
        Assert.state(value.length == 0 || name.length == 0,
                () -> "The name and value attributes of @%s are exclusive".formatted(ClassUtils.getShortName(this.getAnnotationType())));
        return value.length > 0
                ? value
                : name;
    }

    public void collectProperties(PropertyResolver resolver,
                                  List<String> missing,
                                  List<String> nonMatching,
                                  PropertySpecMatcher<V, S> matcher) {
        for (String name : this.getNames()) {
            try {
                String key = this.getPrefix() + name;
                if (resolver.containsProperty(key)) {
                    if (!this.isMatch(resolver.getProperty(key, this.getHavingValueType()), matcher)) {
                        nonMatching.add(name);
                    }
                } else if (!this.isMatchIfMissing()) {
                    missing.add(name);
                }
            } catch (ConversionException e) {
                nonMatching.add(name);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected boolean isMatch(@Nullable V value, PropertySpecMatcher<V, S> matcher) {
        return matcher.compare((S) this, value, this.getHavingValue());
    }

    public Class<? extends Annotation> getAnnotationType() {
        return annotationType;
    }

    public String getPrefix() {
        return prefix;
    }

    public String[] getNames() {
        return names;
    }

    public V getHavingValue() {
        return havingValue;
    }

    public Class<V> getHavingValueType() {
        return havingValueType;
    }

    public boolean isMatchIfMissing() {
        return matchIfMissing;
    }

    public String toString() {
        StringBuilder result = new StringBuilder()
                .append("(")
                .append(this.getPrefix());
        if (this.getNames().length == 1) {
            result.append(this.getNames()[0]);
        } else {
            result.append("[")
                    .append(StringUtils.arrayToCommaDelimitedString(this.getNames()))
                    .append("]");
        }
        return result.append("=")
                .append(this.getHavingValue())
                .append(")")
                .toString();
    }
}
