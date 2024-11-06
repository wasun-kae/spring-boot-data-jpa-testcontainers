package kpd.wasun.playground.jpa.repository;

import kpd.wasun.playground.jpa.entity.Customer;
import kpd.wasun.playground.jpa.testcontainers.PostgresTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@PostgresTestContainer
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setup() {
        customerRepository.deleteAll();
    }

    @Test
    void givenSavedCustomers_whenFindByNameAndPageRequest_thenReturnPageOfCustomer() {
        var johnWick = Customer.builder().firstName("John").lastName("Wick").build();
        var johnWickId = customerRepository.save(johnWick).getId();
        var johnSnow = Customer.builder().firstName("John").lastName("Snow").build();
        var johnSnowId = customerRepository.save(johnSnow).getId();
        var branStark = Customer.builder().firstName("Bran").lastName("Stark").build();
        customerRepository.save(branStark);

        var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "lastName"));
        var pageRequest = PageRequest.of(0, 2, sort);
        var actual = customerRepository.findByFirstName("John", pageRequest);

        assertThat(actual.getTotalPages()).isEqualTo(1);
        assertThat(actual.getTotalElements()).isEqualTo(2);
        assertThat(actual.getContent().size()).isEqualTo(2);

        assertThat(actual.getContent().get(0).getId()).isEqualTo(johnSnowId);
        assertThat(actual.getContent().get(0).getFirstName()).isEqualTo("John");
        assertThat(actual.getContent().get(0).getLastName()).isEqualTo("Snow");

        assertThat(actual.getContent().get(1).getId()).isEqualTo(johnWickId);
        assertThat(actual.getContent().get(1).getFirstName()).isEqualTo("John");
        assertThat(actual.getContent().get(1).getLastName()).isEqualTo("Wick");
    }
}
