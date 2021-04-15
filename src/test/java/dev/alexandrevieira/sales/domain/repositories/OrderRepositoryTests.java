package dev.alexandrevieira.sales.domain.repositories;

import dev.alexandrevieira.sales.domain.entities.Customer;
import dev.alexandrevieira.sales.domain.entities.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderRepositoryTests {
    @Autowired
    OrderRepository repository;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    @Transactional
    public void findCustomerFetchOrdersTest() {
        String customerName = "Alexandre";
        Customer customer = new Customer();
        customer.setName(customerName);

        customer = customerRepository.save(customer);

        List<Order> orders = new ArrayList<>();

        BigDecimal firstValue = BigDecimal.valueOf(35.33);
        LocalDate firstDate = LocalDate.now().minusDays(3);
        BigDecimal secondValue = BigDecimal.valueOf(47.13);
        LocalDate secondDate = LocalDate.now();

        Order order = new Order();
        order.setCustomer(customer);
        order.setTotal(firstValue);
        order.setOrderDate(firstDate);
        orders.add(order);

        order = new Order();
        order.setCustomer(customer);
        order.setTotal(secondValue);
        order.setOrderDate(secondDate);
        orders.add(order);


        orders = repository.saveAll(orders);

        orders = repository.findByCustomer(customer);

        assertThat(orders.get(0).getTotal()).isEqualTo(firstValue);
        assertThat(orders.get(0).getOrderDate()).isEqualTo(firstDate);
        assertThat(orders.get(0).getCustomer().getId()).isEqualTo(customer.getId());

        assertThat(orders.get(1).getTotal()).isEqualTo(secondValue);
        assertThat(orders.get(1).getOrderDate()).isEqualTo(secondDate);
        assertThat(orders.get(1).getCustomer().getId()).isEqualTo(customer.getId());
    }
}
