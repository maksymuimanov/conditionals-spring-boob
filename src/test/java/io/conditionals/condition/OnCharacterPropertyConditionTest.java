package io.conditionals.condition;

import io.conditionals.condition.spec.ComparableMatchType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class OnCharacterPropertyConditionTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    void characterPropertyCondition_shouldCreateBean_whenPropertyEqualsHavingValue() {
        this.contextRunner.withPropertyValues("app.letter=65")
                .withUserConfiguration(EqualsConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(Character.class));
    }

    @Test
    void characterPropertyCondition_shouldNotCreateBean_whenPropertyPresentButDifferent() {
        this.contextRunner.withPropertyValues("app.letter=66")
                .withUserConfiguration(EqualsConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(Character.class));
    }

    @Test
    void characterPropertyCondition_shouldNotCreateBean_whenPropertyMissingAndMatchIfMissingFalse() {
        this.contextRunner.withUserConfiguration(EqualsConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(Character.class));
    }

    @Test
    void characterPropertyCondition_shouldCreateBean_whenPropertyMissingAndMatchIfMissingTrue() {
        this.contextRunner.withUserConfiguration(MatchIfMissingConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(Character.class));
    }

    @Test
    void characterPropertyCondition_shouldCreateBean_whenGreaterThanMatches() {
        this.contextRunner.withPropertyValues("app.letter=90")
                .withUserConfiguration(GreaterThanConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(Character.class));
    }

    @Test
    void characterPropertyCondition_shouldNotCreateBean_whenNotFlagInvertsMatch() {
        this.contextRunner.withPropertyValues("app.letter=65")
                .withUserConfiguration(NotConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(Character.class));
    }

    @Test
    void characterPropertyCondition_shouldCreateBean_whenContainerAnnotationAllMatches() {
        this.contextRunner.withPropertyValues(
                        "app.one=65",
                        "app.two=66"
                ).withUserConfiguration(ContainerAnyMatchesConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(Character.class));
    }

    @Test
    void characterPropertyCondition_shouldNotCreateBean_whenContainerAnnotationNoneMatches() {
        this.contextRunner.withPropertyValues(
                        "app.one=65",
                        "app.two=67"
                ).withUserConfiguration(ContainerAnyMatchesConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(Character.class));
    }

    @Configuration(proxyBeanMethods = false)
    static class EqualsConfig {
        @Bean
        @ConditionalOnCharacterProperty(name = "app.letter", havingValue = 65)
        Character conditionalBean() {
            return 'A';
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class MatchIfMissingConfig {
        @Bean
        @ConditionalOnCharacterProperty(name = "app.letter", havingValue = 65, matchIfMissing = true)
        Character conditionalBean() {
            return 'A';
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class GreaterThanConfig {
        @Bean
        @ConditionalOnCharacterProperty(name = "app.letter", havingValue = 65, matchType = ComparableMatchType.GREATER_THAN)
        Character conditionalBean() {
            return 'A';
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class NotConfig {
        @Bean
        @ConditionalOnCharacterProperty(name = "app.letter", havingValue = 65, not = true)
        Character conditionalBean() {
            return 'A';
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnCharacterProperty(name = "app.one", havingValue = 65)
    @ConditionalOnCharacterProperty(name = "app.two", havingValue = 66)
    static class ContainerAnyMatchesConfig {
        @Bean
        Character conditionalBean() {
            return 'A';
        }
    }
}
