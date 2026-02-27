package io.conditionals.condition;

import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.system.JavaVersion;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(name = "test.enabled", havingValue = "true")
@ConditionalOnBean
@ConditionalOnBooleanProperty
@Configuration
public class TestConfig {
}
