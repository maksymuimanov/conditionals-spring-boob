package io.conditionals.condition;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.ServerSocket;

import static org.assertj.core.api.Assertions.assertThat;

class OnPortAvailableConditionTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    void portAvailableCondition_shouldCreateBean_whenPortIsAvailable() throws IOException {
        int port;
        try (ServerSocket socket = new ServerSocket(8080)) {
            port = socket.getLocalPort();
        }

        this.contextRunner.withUserConfiguration(PortConfig.class)
                .withPropertyValues("app.port=" + port)
                .run(context -> assertThat(context)
                        .hasSingleBean(Integer.class));
    }

    @Test
    void portAvailableCondition_shouldNotCreateBean_whenPortIsNotAvailable() throws IOException {
        try (ServerSocket socket = new ServerSocket(8080)) {
            int port = socket.getLocalPort();

            this.contextRunner.withUserConfiguration(PortConfig.class)
                    .withPropertyValues("app.port=" + port)
                    .run(context -> assertThat(context)
                            .doesNotHaveBean(Integer.class));
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class PortConfig {
        @Bean
        @ConditionalOnPortAvailable(8080)
        Integer conditionalBean() {
            return 1;
        }
    }
}
