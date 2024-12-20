# Spring Boot - Repository Layer Testing with Test Slice `@DataJpaTest` and `TestContainers` (Postgres)

### Pre-requisite

- [JDK version 17](https://openjdk.org/)
- [Docker](https://www.docker.com/)

### Run Testing with Postgres Container

```shell
# Don't forget to start docker service before
./mvnw test
```

### Concerns when using `@DataJpaTest`

Below are default behaviors of this annotation

- Data will be flushed to a database transaction but `NOT` be commited
- It means that the data will `NOT` be completely written in the database and be visible to query directly
- Transaction will be rolled back at the end of `@Test` method execution (to keep each test state clean)

### References

- [Medium :: [Spring Boot] Configure TestContainers in your test code this way](https://jskim1991.medium.com/spring-boot-configure-testcontainers-in-your-test-code-this-way-417b221e55b)
- [Stackoverflow :: How to combine Testcontainers with @DataJpaTest avoiding code duplication?](https://stackoverflow.com/questions/68602204/how-to-combine-testcontainers-with-datajpatest-avoiding-code-duplication)