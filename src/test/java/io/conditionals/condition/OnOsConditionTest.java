package io.conditionals.condition;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class OnOsConditionTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    void osCondition_shouldCreateBean_whenOsNameContainsAnyCandidate() {
        this.contextRunner.withPropertyValues("os.name=Windows 11")
                .withUserConfiguration(WindowsConfig.class)
                .run(context -> assertThat(context)
                        .hasSingleBean(String.class));
    }

    @Test
    void osCondition_shouldNotCreateBean_whenOsNameDoesNotContainAnyCandidate() {
        this.contextRunner.withPropertyValues("os.name=Linux")
                .withUserConfiguration(WindowsConfig.class)
                .run(context -> assertThat(context)
                        .doesNotHaveBean(String.class));
    }

    @Test
    void osCondition_shouldNotCreateBean_whenPropertyMissingAndSystemPropertyDoesNotMatch() {
        String original = System.getProperty("os.name");
        try {
            System.setProperty("os.name", "Solaris");
            this.contextRunner.withUserConfiguration(WindowsConfig.class)
                    .run(context -> assertThat(context)
                            .doesNotHaveBean(String.class));
        } finally {
            if (original == null) {
                System.clearProperty("os.name");
            } else {
                System.setProperty("os.name", original);
            }
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class WindowsConfig {
        @Bean
        @ConditionalOnOs({"windows", "mac"})
        String conditionalBean() {
            return "OK";
        }
    }
}
