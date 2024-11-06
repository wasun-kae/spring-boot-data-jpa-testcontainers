package kpd.wasun.playground.jpa.repository;

import kpd.wasun.playground.jpa.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    List<Customer> findTop10ByFirstNameOrderByLastNameAsc(String firstName);

    /* Note: @OneToMany fetching isn't compatible with Spring Data Page */
    Page<Customer> findByFirstName(String firstName, PageRequest pageRequest);
}
