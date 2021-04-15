package dev.alexandrevieira.sales.domain.repositories;

import dev.alexandrevieira.sales.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(" select c from Customer c left join fetch c.orders where c.id = :id ")
    Optional<Customer> findCustomerFetchOrders(@Param("id") Long id);
}
