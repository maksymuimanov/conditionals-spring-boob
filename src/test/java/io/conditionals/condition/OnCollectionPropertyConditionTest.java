package io.conditionals.condition;

import io.conditionals.condition.spec.CollectionMatchType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class OnCollectionPropertyConditionTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    void collectionPropertyCondition_shouldCreateBean_whenMatchTypeEqualsMatches() {
        this.contextRunner.withPropertyValues("app.tags=a, b")
                .withUserConfiguration(EqualsConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldNotCreateBean_whenMatchTypeEqualsDoesNotMatch() {
        this.contextRunner.withPropertyValues("app.tags=a, c")
                .withUserConfiguration(EqualsConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldCreateBean_whenMatchTypeContainsAnyMatches() {
        this.contextRunner.withPropertyValues("app.tags=a, b")
                .withUserConfiguration(ContainsAnyConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldNotCreateBean_whenMatchTypeContainsAnyDoesNotMatch() {
        this.contextRunner.withPropertyValues("app.tags=x, y")
                .withUserConfiguration(ContainsAnyConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldCreateBean_whenMatchTypeContainsAllMatches() {
        this.contextRunner.withPropertyValues("app.tags=a, b, c")
                .withUserConfiguration(ContainsAllConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldNotCreateBean_whenMatchTypeContainsAllDoesNotMatch() {
        this.contextRunner.withPropertyValues("app.tags=a, b")
                .withUserConfiguration(ContainsAllConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldCreateBean_whenMatchTypeContainsSequenceMatches() {
        this.contextRunner.withPropertyValues("app.tags=a, b, c")
                .withUserConfiguration(ContainsSequenceConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldNotCreateBean_whenMatchTypeContainsSequenceDoesNotMatch() {
        this.contextRunner.withPropertyValues("app.tags=a, c, b")
                .withUserConfiguration(ContainsSequenceConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldCreateBean_whenMatchTypeStartsWithAnyMatches() {
        this.contextRunner.withPropertyValues("app.tags=a, b")
                .withUserConfiguration(StartsWithAnyConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldNotCreateBean_whenMatchTypeStartsWithAnyDoesNotMatch() {
        this.contextRunner.withPropertyValues("app.tags=x, a")
                .withUserConfiguration(StartsWithAnyConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldCreateBean_whenMatchTypeStartsWithAllMatches() {
        this.contextRunner.withPropertyValues("app.tags=a, b, c")
                .withUserConfiguration(StartsWithAllConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldNotCreateBean_whenMatchTypeStartsWithAllDoesNotMatch() {
        this.contextRunner.withPropertyValues("app.tags=a, c, b")
                .withUserConfiguration(StartsWithAllConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldCreateBean_whenMatchTypeEndsWithAnyMatches() {
        this.contextRunner.withPropertyValues("app.tags=a, b")
                .withUserConfiguration(EndsWithAnyConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldNotCreateBean_whenMatchTypeEndsWithAnyDoesNotMatch() {
        this.contextRunner.withPropertyValues("app.tags=b, x")
                .withUserConfiguration(EndsWithAnyConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldCreateBean_whenMatchTypeEndsWithAllMatches() {
        this.contextRunner.withPropertyValues("app.tags=a, b")
                .withUserConfiguration(EndsWithAllConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldNotCreateBean_whenMatchTypeEndsWithAllDoesNotMatch() {
        this.contextRunner.withPropertyValues("app.tags=x, b, a")
                .withUserConfiguration(EndsWithAllConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldNotCreateBean_whenPropertyMissingAndMatchIfMissingFalse() {
        this.contextRunner.withUserConfiguration(EqualsConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldCreateBean_whenPropertyMissingAndMatchIfMissingTrue() {
        this.contextRunner.withUserConfiguration(MatchIfMissingConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldNotCreateBean_whenNotFlagInvertsMatch() {
        this.contextRunner.withPropertyValues("app.tags=a, b")
                .withUserConfiguration(NotConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldCreateBean_whenSizeMatches() {
        this.contextRunner.withPropertyValues("app.tags=a, b")
                .withUserConfiguration(SizeConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldNotCreateBean_whenSizeDoesNotMatch() {
        this.contextRunner.withPropertyValues("app.tags=a, b, c")
                .withUserConfiguration(SizeConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldCreateBean_whenContainerAnnotationAllMatches() {
        this.contextRunner.withPropertyValues("app.one=a", "app.two=b")
                .withUserConfiguration(ContainerAnyMatchesConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(String[].class));
    }

    @Test
    void collectionPropertyCondition_shouldNotCreateBean_whenContainerAnnotationNoneMatches() {
        this.contextRunner.withPropertyValues("app.one=x", "app.two=b")
                .withUserConfiguration(ContainerAnyMatchesConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(String[].class));
    }

    @Configuration(proxyBeanMethods = false)
    static class EqualsConfig {
        @Bean
        @ConditionalOnCollectionProperty(name = "app.tags", havingValue = {"a", "b"}, matchType = CollectionMatchType.EQUALS)
        String[] conditionalBean() {
            return new String[]{"a", "b"};
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class ContainsAnyConfig {
        @Bean
        @ConditionalOnCollectionProperty(name = "app.tags", havingValue = {"b"}, matchType = CollectionMatchType.CONTAINS_ANY)
        String[] conditionalBean() {
            return new String[]{"a", "b"};
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class ContainsAllConfig {
        @Bean
        @ConditionalOnCollectionProperty(name = "app.tags", havingValue = {"a", "c"}, matchType = CollectionMatchType.CONTAINS_ALL)
        String[] conditionalBean() {
            return new String[]{"a", "b", "c"};
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class ContainsSequenceConfig {
        @Bean
        @ConditionalOnCollectionProperty(name = "app.tags", havingValue = {"b", "c"}, matchType = CollectionMatchType.CONTAINS_SEQUENCE)
        String[] conditionalBean() {
            return new String[]{"a", "b", "c"};
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class StartsWithAnyConfig {
        @Bean
        @ConditionalOnCollectionProperty(name = "app.tags", havingValue = {"a"}, matchType = CollectionMatchType.STARTS_WITH_ANY)
        String[] conditionalBean() {
            return new String[]{"a", "b"};
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class StartsWithAllConfig {
        @Bean
        @ConditionalOnCollectionProperty(name = "app.tags", havingValue = {"a", "b"}, matchType = CollectionMatchType.STARTS_WITH_ALL)
        String[] conditionalBean() {
            return new String[]{"a", "b", "c"};
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class EndsWithAnyConfig {
        @Bean
        @ConditionalOnCollectionProperty(name = "app.tags", havingValue = {"b"}, matchType = CollectionMatchType.ENDS_WITH_ANY)
        String[] conditionalBean() {
            return new String[]{"a", "b"};
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class EndsWithAllConfig {
        @Bean
        @ConditionalOnCollectionProperty(name = "app.tags", havingValue = {"a", "b"}, matchType = CollectionMatchType.ENDS_WITH_ALL)
        String[] conditionalBean() {
            return new String[]{"x", "a", "b"};
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class SizeConfig {
        @Bean
        @ConditionalOnCollectionProperty(name = "app.tags", havingValue = {"a"}, size = 2, matchType = CollectionMatchType.CONTAINS_ANY)
        String[] conditionalBean() {
            return new String[]{"a", "b"};
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class MatchIfMissingConfig {
        @Bean
        @ConditionalOnCollectionProperty(name = "app.tags", havingValue = {"a"}, matchIfMissing = true)
        String[] conditionalBean() {
            return new String[]{"a"};
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class NotConfig {
        @Bean
        @ConditionalOnCollectionProperty(name = "app.tags", havingValue = {"a", "b"}, not = true)
        String[] conditionalBean() {
            return new String[]{"a", "b"};
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnCollectionProperty(name = "app.one", havingValue = {"a"}, matchType = CollectionMatchType.CONTAINS_ANY)
    @ConditionalOnCollectionProperty(name = "app.two", havingValue = {"b"}, matchType = CollectionMatchType.CONTAINS_ANY)
    static class ContainerAnyMatchesConfig {
        @Bean
        String[] conditionalBean() {
            return new String[]{"a", "b"};
        }
    }
}
