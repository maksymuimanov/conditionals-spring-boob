package io.conditionals.condition;

import io.conditionals.condition.spec.ComparableMatchType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class OnDurationPropertyConditionTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    void durationPropertyCondition_shouldCreateBean_whenPropertyEqualsHavingValue() {
        this.contextRunner.withPropertyValues("app.timeout=10s")
                .withUserConfiguration(EqualsConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(Duration.class));
    }

    @Test
    void durationPropertyCondition_shouldNotCreateBean_whenPropertyPresentButDifferent() {
        this.contextRunner.withPropertyValues("app.timeout=11s")
                .withUserConfiguration(EqualsConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(Duration.class));
    }

    @Test
    void durationPropertyCondition_shouldNotCreateBean_whenPropertyMissingAndMatchIfMissingFalse() {
        this.contextRunner.withUserConfiguration(EqualsConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(Duration.class));
    }

    @Test
    void durationPropertyCondition_shouldCreateBean_whenPropertyMissingAndMatchIfMissingTrue() {
        this.contextRunner.withUserConfiguration(MatchIfMissingConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(Duration.class));
    }

    @Test
    void durationPropertyCondition_shouldCreateBean_whenGreaterThanMatches() {
        this.contextRunner.withPropertyValues("app.timeout=2m")
                .withUserConfiguration(GreaterThanConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(Duration.class));
    }

    @Test
    void durationPropertyCondition_shouldNotCreateBean_whenNotFlagInvertsMatch() {
        this.contextRunner.withPropertyValues("app.timeout=10s")
                .withUserConfiguration(NotConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(Duration.class));
    }

    @Test
    void durationPropertyCondition_shouldCreateBean_whenContainerAnnotationAllMatches() {
        this.contextRunner.withPropertyValues("app.one=10s", "app.two=5s")
                .withUserConfiguration(ContainerAllMatchesConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(Duration.class));
    }

    @Test
    void durationPropertyCondition_shouldNotCreateBean_whenContainerAnnotationNoneMatches() {
        this.contextRunner.withPropertyValues(
                        "app.one=10s",
                        "app.two=6s"
                ).withUserConfiguration(ContainerAllMatchesConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(Duration.class));
    }

    @Configuration(proxyBeanMethods = false)
    static class EqualsConfig {
        @Bean
        @ConditionalOnDurationProperty(name = "app.timeout", havingValue = "10s")
        Duration conditionalBean() {
            return Duration.ofSeconds(10);
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class MatchIfMissingConfig {
        @Bean
        @ConditionalOnDurationProperty(name = "app.timeout", havingValue = "10s", matchIfMissing = true)
        Duration conditionalBean() {
            return Duration.ofSeconds(10);
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class GreaterThanConfig {
        @Bean
        @ConditionalOnDurationProperty(name = "app.timeout", havingValue = "10s", matchType = ComparableMatchType.GREATER_THAN)
        Duration conditionalBean() {
            return Duration.ofSeconds(10);
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class NotConfig {
        @Bean
        @ConditionalOnDurationProperty(name = "app.timeout", havingValue = "10s", not = true)
        Duration conditionalBean() {
            return Duration.ofSeconds(10);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnDurationProperty(name = "app.one", havingValue = "10s")
    @ConditionalOnDurationProperty(name = "app.two", havingValue = "5s")
    static class ContainerAllMatchesConfig {
        @Bean
        Duration conditionalBean() {
            return Duration.ofSeconds(10);
        }
    }
}
