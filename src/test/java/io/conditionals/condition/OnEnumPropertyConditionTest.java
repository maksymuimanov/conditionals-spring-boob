package io.conditionals.condition;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class OnEnumPropertyConditionTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    void enumPropertyCondition_shouldCreateBean_whenPropertyMatchesEnumValueIgnoringCase() {
        this.contextRunner.withPropertyValues("app.mode=dev")
                .withUserConfiguration(ModeDevConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(Mode.class));
    }

    @Test
    void enumPropertyCondition_shouldNotCreateBean_whenPropertyDoesNotMatchEnumValue() {
        this.contextRunner.withPropertyValues("app.mode=prod")
                .withUserConfiguration(ModeDevConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(Mode.class));
    }

    @Test
    void enumPropertyCondition_shouldNotCreateBean_whenPropertyIsNotValidEnumConstant() {
        this.contextRunner.withPropertyValues("app.mode=unknown")
                .withUserConfiguration(ModeDevConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(Mode.class));
    }

    @Test
    void enumPropertyCondition_shouldCreateBean_whenPropertyMissingAndMatchIfMissingTrue() {
        this.contextRunner.withUserConfiguration(MatchIfMissingConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(Mode.class));
    }

    @Test
    void enumPropertyCondition_shouldCreateBean_whenContainerAnnotationAllMatches() {
        this.contextRunner.withPropertyValues(
                "app.one=dev",
                "app.two=dev"
        ).withUserConfiguration(ContainerAnyMatchesConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(Mode.class));
    }

    @Test
    void enumPropertyCondition_shouldNotCreateBean_whenContainerAnnotationNoneMatches() {
        this.contextRunner.withPropertyValues(
                "app.one=dev",
                "app.two=prod"
        ).withUserConfiguration(ContainerAnyMatchesConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(Mode.class));
    }

    enum Mode {
        DEV,
        PROD
    }

    @Configuration(proxyBeanMethods = false)
    static class ModeDevConfig {
        @Bean
        @ConditionalOnEnumProperty(name = "app.mode", havingValue = "DEV", enumType = Mode.class)
        Mode conditionalBean() {
            return Mode.DEV;
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class MatchIfMissingConfig {
        @Bean
        @ConditionalOnEnumProperty(name = "app.mode", havingValue = "DEV", enumType = Mode.class, matchIfMissing = true)
        Mode conditionalBean() {
            return Mode.DEV;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnEnumProperty(name = "app.one", havingValue = "DEV", enumType = Mode.class)
    @ConditionalOnEnumProperty(name = "app.two", havingValue = "dev", enumType = Mode.class)
    static class ContainerAnyMatchesConfig {
        @Bean
        Mode conditionalBean() {
            return Mode.DEV;
        }
    }
}
