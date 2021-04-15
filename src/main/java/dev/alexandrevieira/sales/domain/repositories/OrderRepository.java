package dev.alexandrevieira.sales.domain.repositories;

import dev.alexandrevieira.sales.domain.entities.Customer;
import dev.alexandrevieira.sales.domain.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
}
