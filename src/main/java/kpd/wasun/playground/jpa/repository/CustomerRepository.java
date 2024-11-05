package kpd.wasun.playground.jpa.repository;

import kpd.wasun.playground.jpa.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Page<Customer> findByFirstName(String FirstName, PageRequest pageRequest);
}
