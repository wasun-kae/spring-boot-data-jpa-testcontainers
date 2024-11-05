package kpd.wasun.playground.jpa;

import kpd.wasun.playground.jpa.testcontainers.PostgresTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@PostgresTestContainer
class SpringBootDataJpaApplicationTests {

    @Test
    void contextLoads() {

    }
}
