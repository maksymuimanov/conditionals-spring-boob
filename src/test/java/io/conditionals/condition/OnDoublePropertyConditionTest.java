package io.conditionals.condition;

import io.conditionals.condition.spec.ComparableMatchType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class OnDoublePropertyConditionTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    void doublePropertyCondition_shouldCreateBean_whenPropertyEqualsHavingValue() {
        this.contextRunner.withPropertyValues("app.ratio=1")
                .withUserConfiguration(EqualsConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(Double.class));
    }

    @Test
    void doublePropertyCondition_shouldNotCreateBean_whenPropertyNotEqualsHavingValue() {
        this.contextRunner.withPropertyValues("app.ratio=1.1")
                .withUserConfiguration(EqualsConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(Double.class));
    }

    @Test
    void doublePropertyCondition_shouldNotCreateBean_whenPropertyIsNaN() {
        this.contextRunner.withPropertyValues("app.ratio=NaN")
                .withUserConfiguration(EqualsConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(Double.class));
    }

    @Test
    void doublePropertyCondition_shouldCreateBean_whenGreaterThanMatches() {
        this.contextRunner.withPropertyValues("app.ratio=2.0")
                .withUserConfiguration(GreaterThanConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(Double.class));
    }

    @Test
    void doublePropertyCondition_shouldNotCreateBean_whenNotFlagInvertsMatch() {
        this.contextRunner.withPropertyValues("app.ratio=1.0")
                .withUserConfiguration(NotConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(Double.class));
    }

    @Test
    void doublePropertyCondition_shouldCreateBean_whenPropertyMissingAndMatchIfMissingTrue() {
        this.contextRunner.withUserConfiguration(MatchIfMissingConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(Double.class));
    }

    @Test
    void doublePropertyCondition_shouldCreateBean_whenContainerAnnotationAllMatches() {
        this.contextRunner.withPropertyValues(
                        "app.one=3",
                        "app.two=2"
                ).withUserConfiguration(ContainerAnyMatchesConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(Double.class));
    }

    @Test
    void doublePropertyCondition_shouldNotCreateBean_whenContainerAnnotationNoneMatches() {
        this.contextRunner.withPropertyValues(
                        "app.one=1",
                        "app.two=2"
                ).withUserConfiguration(ContainerAnyMatchesConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(Double.class));
    }

    @Configuration(proxyBeanMethods = false)
    static class EqualsConfig {
        @Bean
        @ConditionalOnDoubleProperty(name = "app.ratio", havingValue = 1.0)
        Double conditionalBean() {
            return 1.0;
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class GreaterThanConfig {
        @Bean
        @ConditionalOnDoubleProperty(name = "app.ratio", havingValue = 1.0, matchType = ComparableMatchType.GREATER_THAN)
        Double conditionalBean() {
            return 1.0;
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class NotConfig {
        @Bean
        @ConditionalOnDoubleProperty(name = "app.ratio", havingValue = 1.0, not = true)
        Double conditionalBean() {
            return 1.0;
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class MatchIfMissingConfig {
        @Bean
        @ConditionalOnDoubleProperty(name = "app.ratio", havingValue = 1.0, matchIfMissing = true)
        Double conditionalBean() {
            return 1.0;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnDoubleProperty(name = "app.one", havingValue = 3.0)
    @ConditionalOnDoubleProperty(name = "app.two", havingValue = 2.0)
    static class ContainerAnyMatchesConfig {
        @Bean
        Double conditionalBean() {
            return 1.0;
        }
    }
}
