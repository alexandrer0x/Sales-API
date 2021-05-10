package dev.alexandrevieira.sales.api.resources;

import dev.alexandrevieira.sales.api.dtos.CustomerDTO;
import dev.alexandrevieira.sales.api.dtos.CustomerWithoutOrdersDTO;
import dev.alexandrevieira.sales.domain.entities.Customer;
import dev.alexandrevieira.sales.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "customers")
public class CustomerResource {
    @Autowired
    private CustomerService service;


    @GetMapping(path = "{id}")
    @ResponseStatus(OK)
    public CustomerWithoutOrdersDTO findWithoutOrders(@PathVariable Long id) {
        CustomerWithoutOrdersDTO customer = service.findWithoutOrders(id);

        return customer;
    }

    @GetMapping(path = "{id}/orders")
    @ResponseStatus(OK)
    public CustomerDTO findWithOrders(@PathVariable Long id) {
        CustomerDTO customer = service.findWithOrders(id);

        return customer;
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<CustomerDTO> filter(CustomerDTO filter) {
        Customer customer = filter.toEntity();
        List<Customer> list = service.filter(customer);

        return list.stream().map(x -> x.toDTO()).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public CustomerDTO save(@RequestBody @Valid CustomerDTO dto) {
        Customer customer = dto.toEntity();
        return service.save(customer).toDTO();
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping(path = "{id}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody CustomerDTO dto) {
        Customer customer = dto.toEntity();
        service.update(id, customer);
    }

}
