package dev.alexandrevieira.sales.services;

import dev.alexandrevieira.sales.domain.entities.Customer;
import dev.alexandrevieira.sales.domain.repositories.CustomerRepository;
import dev.alexandrevieira.sales.exceptions.BusinessRuleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class CustomerService extends GenericEntityService<Customer, Long, CustomerRepository> {
    public CustomerService(CustomerRepository customerRepository) {
        super(customerRepository);
    }

    public Customer findWithoutOrders(Long id) {
        log.info(this.getClass().getSimpleName() + ".findWithoutOrders(Long id)");
        Customer customer = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return customer;
    }

    public Customer findWithOrders(Long id) {
        log.info(this.getClass().getSimpleName() + ".findWithOrders(Long id)");
        Customer customer = ((CustomerRepository) repository).findOneById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return customer;
    }

    public Customer save(Customer customer) {
        log.info(this.getClass().getSimpleName() + ".save(Customer customer)");

        //if already exists a Customer with given CPF
        if (((CustomerRepository) repository).existsByCpf(customer.getCpf())) {
            throw new BusinessRuleException("CPF already associated with an account");
        }
        customer.setId(null);

        return repository.save(customer);
    }
}
