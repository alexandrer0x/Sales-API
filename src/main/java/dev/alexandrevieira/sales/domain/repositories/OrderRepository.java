package dev.alexandrevieira.sales.domain.repositories;

import dev.alexandrevieira.sales.domain.entities.Customer;
import dev.alexandrevieira.sales.domain.entities.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "orderItems",
                    "orderItems.product",
                    "customer"
            }
    )
    Optional<Order> findOneById(Long id);


    List<Order> findByCustomer(Customer customer);
}
