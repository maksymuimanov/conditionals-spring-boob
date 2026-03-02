package io.conditionals.condition;

import io.conditionals.condition.spec.ComparableMatchType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class OnLongPropertyConditionTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    void longPropertyCondition_shouldCreateBean_whenPropertyEqualsHavingValue() {
        this.contextRunner.withPropertyValues("app.count=5")
                .withUserConfiguration(EqualsConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(Long.class));
    }

    @Test
    void longPropertyCondition_shouldNotCreateBean_whenPropertyPresentButDifferent() {
        this.contextRunner.withPropertyValues("app.count=4")
                .withUserConfiguration(EqualsConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(Long.class));
    }

    @Test
    void longPropertyCondition_shouldNotCreateBean_whenPropertyMissingAndMatchIfMissingFalse() {
        this.contextRunner.withUserConfiguration(EqualsConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(Long.class));
    }

    @Test
    void longPropertyCondition_shouldCreateBean_whenPropertyMissingAndMatchIfMissingTrue() {
        this.contextRunner.withUserConfiguration(MatchIfMissingConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(Long.class));
    }

    @Test
    void longPropertyCondition_shouldCreateBean_whenGreaterThanMatches() {
        this.contextRunner.withPropertyValues("app.count=10")
                .withUserConfiguration(GreaterThanConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(Long.class));
    }

    @Test
    void longPropertyCondition_shouldNotCreateBean_whenNotFlagInvertsMatch() {
        this.contextRunner.withPropertyValues("app.count=5")
                .withUserConfiguration(NotConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(Long.class));
    }

    @Test
    void longPropertyCondition_shouldCreateBean_whenContainerAnnotationAllMatches() {
        this.contextRunner.withPropertyValues(
                        "app.one=3",
                        "app.two=2"
                ).withUserConfiguration(ContainerAnyMatchesConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(Long.class));
    }

    @Test
    void longPropertyCondition_shouldNotCreateBean_whenContainerAnnotationNoneMatches() {
        this.contextRunner.withPropertyValues(
                        "app.one=1",
                        "app.two=2"
                ).withUserConfiguration(ContainerAnyMatchesConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(Long.class));
    }

    @Configuration(proxyBeanMethods = false)
    static class EqualsConfig {
        @Bean
        @ConditionalOnLongProperty(name = "app.count", havingValue = 5)
        Long conditionalBean() {
            return 5L;
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class MatchIfMissingConfig {
        @Bean
        @ConditionalOnLongProperty(name = "app.count", havingValue = 5, matchIfMissing = true)
        Long conditionalBean() {
            return 5L;
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class GreaterThanConfig {
        @Bean
        @ConditionalOnLongProperty(name = "app.count", havingValue = 5, matchType = ComparableMatchType.GREATER_THAN)
        Long conditionalBean() {
            return 5L;
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class NotConfig {
        @Bean
        @ConditionalOnLongProperty(name = "app.count", havingValue = 5, not = true)
        Long conditionalBean() {
            return 5L;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnLongProperty(name = "app.one", havingValue = 3)
    @ConditionalOnLongProperty(name = "app.two", havingValue = 2)
    static class ContainerAnyMatchesConfig {
        @Bean
        Long conditionalBean() {
            return 5L;
        }
    }
}
