package kpd.wasun.playground.jpa.testcontainers;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

public class PostgresTestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.4");

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        container.start();

        var environment = applicationContext.getEnvironment();
        var testEnvToValue = Map.of(
                "spring.datasource.url", container.getJdbcUrl(),
                "spring.datasource.username", container.getUsername(),
                "spring.datasource.password", container.getPassword()
        );

        TestPropertyValues.of(testEnvToValue).applyTo(environment);
    }
}
