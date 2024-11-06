package kpd.wasun.playground.jpa.repository;

import kpd.wasun.playground.jpa.entity.Address;
import kpd.wasun.playground.jpa.entity.Customer;
import kpd.wasun.playground.jpa.testcontainers.PostgresTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Set;

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

    private Customer saveCustomerToDatabase(String firstName, String lastName, boolean containedAddress) {
        var addresses = containedAddress ?
                Set.of(Address.builder()
                        .name("address")
                        .zipCode("1150")
                        .build()
                ) : null;

        var customer = Customer.builder()
                .firstName(firstName)
                .lastName(lastName)
                .addresses(addresses)
                .build();

        return customerRepository.save(customer);
    }

    @Test
    void givenSavedCustomerWithAddress_whenFindById_thenReturnCustomerWithAddress() {
        var existingCustomer = saveCustomerToDatabase("John", "Wick", true);
        var actual = customerRepository.findById(existingCustomer.getId());

        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getId()).isEqualTo(existingCustomer.getId());
        assertThat(actual.get().getAddresses()).hasSize(1);
        assertThat(actual.get().getAddresses().stream().allMatch(address -> address.getId() != null)).isTrue();
    }

    @Test
    void givenSavedCustomerWithoutAddress_whenFindById_thenReturnCustomerWithoutAddress() {
        var existingCustomer = saveCustomerToDatabase("John", "Wick", false);
        var actual = customerRepository.findById(existingCustomer.getId());

        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getId()).isEqualTo(existingCustomer.getId());
        assertThat(actual.get().getAddresses()).isNull();
    }

    @Test
    void givenSavedCustomers_whenFindTop10ByFirstNameOrderByLastNameAsc_thenReturnCustomers() {
        var johnWick = saveCustomerToDatabase("John", "Wick", false);
        var johnSnow = saveCustomerToDatabase("John", "Snow", true);
        saveCustomerToDatabase("Bran", "Stark", false);

        var actual = customerRepository.findTop10ByFirstNameOrderByLastNameAsc("John");

        assertThat(actual.get(0).getId()).isEqualTo(johnSnow.getId());
        assertThat(actual.get(0).getFirstName()).isEqualTo("John");
        assertThat(actual.get(0).getLastName()).isEqualTo("Snow");
        assertThat(actual.get(0).getAddresses()).hasSize(1);
        assertThat(actual.get(0).getAddresses().stream().allMatch(address -> address.getId() != null)).isTrue();

        assertThat(actual.get(1).getId()).isEqualTo(johnWick.getId());
        assertThat(actual.get(1).getFirstName()).isEqualTo("John");
        assertThat(actual.get(1).getLastName()).isEqualTo("Wick");
        assertThat(actual.get(1).getAddresses()).isNull();
    }

    @Test
    void givenSavedCustomers_whenFindByNameAndPageRequest_thenReturnPageOfCustomer() {
        var johnWick = saveCustomerToDatabase("John", "Wick", false);
        var johnSnow = saveCustomerToDatabase("John", "Snow", false);
        saveCustomerToDatabase("Bran", "Stark", false);

        var sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "lastName"));
        var pageRequest = PageRequest.of(0, 2, sort);
        var actual = customerRepository.findByFirstName("John", pageRequest);

        assertThat(actual.getTotalPages()).isEqualTo(1);
        assertThat(actual.getTotalElements()).isEqualTo(2);
        assertThat(actual.getContent().size()).isEqualTo(2);

        assertThat(actual.getContent().get(0).getId()).isEqualTo(johnSnow.getId());
        assertThat(actual.getContent().get(0).getFirstName()).isEqualTo("John");
        assertThat(actual.getContent().get(0).getLastName()).isEqualTo("Snow");

        assertThat(actual.getContent().get(1).getId()).isEqualTo(johnWick.getId());
        assertThat(actual.getContent().get(1).getFirstName()).isEqualTo("John");
        assertThat(actual.getContent().get(1).getLastName()).isEqualTo("Wick");
    }
}
