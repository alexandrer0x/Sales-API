package dev.alexandrevieira.sales.domain.repositories;

import dev.alexandrevieira.sales.domain.entities.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @EntityGraph(value = "fetchOrders")
    Optional<Customer> findOneById(Long id);

    boolean existsByCpf(String cpf);
}
