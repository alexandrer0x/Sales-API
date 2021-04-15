package dev.alexandrevieira.sales.domain.repositories;

import dev.alexandrevieira.sales.domain.entities.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomerRepositoryTests {
    @Autowired
    CustomerRepository repository;

    @Test
    @Transactional
    public void findCustomerFetchOrdersTest() {
        String customerName = "Alexandre";
        Customer customer = new Customer();
        customer.setName(customerName);

        customer = repository.save(customer);

        Customer result = repository.findCustomerFetchOrders(customer.getId()).get();

        //repository.deleteById(result.getId());

        assertThat(customer.getId()).isEqualTo(result.getId());
        assertThat(customer.getName()).isEqualTo(result.getName());
        assertThat(result.getOrders()).isNotNull();
    }
}
