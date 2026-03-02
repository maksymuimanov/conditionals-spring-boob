package io.conditionals.condition;

import io.conditionals.condition.spec.MapMatchType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class OnMapPropertyConditionTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    void mapPropertyCondition_shouldCreateBean_whenMatchTypeEqualsMatches() {
        this.contextRunner.withPropertyValues("app.props.k1=v1", "app.props.k2=v2")
                .withUserConfiguration(EqualsConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(String.class));
    }

    @Test
    void mapPropertyCondition_shouldNotCreateBean_whenMatchTypeEqualsDoesNotMatch() {
        this.contextRunner.withPropertyValues("app.props.k1=v1")
                .withUserConfiguration(EqualsConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(String.class));
    }

    @Test
    void mapPropertyCondition_shouldCreateBean_whenMatchTypeContainsAnyMatches() {
        this.contextRunner.withPropertyValues("app.props.k1=v1", "app.props.k2=v2")
                .withUserConfiguration(ContainsAnyConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(String.class));
    }

    @Test
    void mapPropertyCondition_shouldNotCreateBean_whenMatchTypeContainsAnyDoesNotMatch() {
        this.contextRunner.withPropertyValues("app.props.k1=v1")
                .withUserConfiguration(ContainsAnyConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(String.class));
    }

    @Test
    void mapPropertyCondition_shouldCreateBean_whenMatchTypeContainsAllMatches() {
        this.contextRunner.withPropertyValues("app.props.k1=v1", "app.props.k2=v2")
                .withUserConfiguration(ContainsAllConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(String.class));
    }

    @Test
    void mapPropertyCondition_shouldNotCreateBean_whenMatchTypeContainsAllDoesNotMatch() {
        this.contextRunner.withPropertyValues("app.props.k1=v1")
                .withUserConfiguration(ContainsAllConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(String.class));
    }

    @Test
    void mapPropertyCondition_shouldNotCreateBean_whenPropertyMissingAndMatchIfMissingFalse() {
        this.contextRunner.withUserConfiguration(EqualsConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(String.class));
    }

    @Test
    void mapPropertyCondition_shouldCreateBean_whenPropertyMissingAndMatchIfMissingTrue() {
        this.contextRunner.withUserConfiguration(MatchIfMissingConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(String.class));
    }

    @Test
    void mapPropertyCondition_shouldNotCreateBean_whenNotFlagInvertsMatch() {
        this.contextRunner.withPropertyValues("app.props.k1=v1")
                .withUserConfiguration(NotConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(String.class));
    }

    @Test
    void mapPropertyCondition_shouldCreateBean_whenContainerAnnotationAllMatches() {
        this.contextRunner.withPropertyValues(
                        "app.one.k=v",
                        "app.two.k=v"
                ).withUserConfiguration(ContainerAnyMatchesConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(String.class));
    }

    @Test
    void mapPropertyCondition_shouldNotCreateBean_whenContainerAnnotationNoneMatches() {
        this.contextRunner.withPropertyValues(
                        "app.one.k=v",
                        "app.two.k=nope"
                ).withUserConfiguration(ContainerAnyMatchesConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(String.class));
    }

    @Test
    void mapPropertyCondition_shouldFailContext_whenCandidateKeysAndValuesLengthMismatch() {
        this.contextRunner.withPropertyValues("app.props.k1=v1")
                .withUserConfiguration(InvalidCandidateConfig.class)
                .run(context -> {
                    assertThat(context)
                            .hasFailed();
                });
    }

    @Configuration(proxyBeanMethods = false)
    static class EqualsConfig {
        @Bean
        @ConditionalOnMapProperty(name = "app.props", havingValue = {"k1", "v1", "k2", "v2"}, matchType = MapMatchType.EQUALS)
        String conditionalBean() {
            return "OK";
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class ContainsAnyConfig {
        @Bean
        @ConditionalOnMapProperty(name = "app.props", havingValue = {"k2", "v2"}, matchType = MapMatchType.CONTAINS_ANY)
        String conditionalBean() {
            return "OK";
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class ContainsAllConfig {
        @Bean
        @ConditionalOnMapProperty(name = "app.props", havingValue = {"k1", "v1", "k2", "v2"}, matchType = MapMatchType.CONTAINS_ALL)
        String conditionalBean() {
            return "OK";
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class MatchIfMissingConfig {
        @Bean
        @ConditionalOnMapProperty(name = "app.props", havingValue = {"k", "v"}, matchIfMissing = true)
        String conditionalBean() {
            return "OK";
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class NotConfig {
        @Bean
        @ConditionalOnMapProperty(name = "app.props", havingValue = {"k", "v"}, not = true)
        String conditionalBean() {
            return "OK";
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMapProperty(name = "app.one", havingValue = {"k", "v"})
    @ConditionalOnMapProperty(name = "app.two", havingValue = {"k", "v"})
    static class ContainerAnyMatchesConfig {
        @Bean
        String conditionalBean() {
            return "OK";
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class InvalidCandidateConfig {
        @Bean
        @ConditionalOnMapProperty(name = "app.props", havingValue = {"k"})
        String conditionalBean() {
            return "OK";
        }
    }
}
