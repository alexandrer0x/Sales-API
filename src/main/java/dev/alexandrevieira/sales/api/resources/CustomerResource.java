package dev.alexandrevieira.sales.api.resources;

import dev.alexandrevieira.sales.api.dtos.CustomerRequestDTO;
import dev.alexandrevieira.sales.api.dtos.CustomerResponseWithOrdersDTO;
import dev.alexandrevieira.sales.api.dtos.CustomerResponseWithoutOrdersDTO;
import dev.alexandrevieira.sales.api.exception.ApiError;
import dev.alexandrevieira.sales.domain.entities.Customer;
import dev.alexandrevieira.sales.services.CustomerService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "customers", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerResource {
    @Autowired
    private CustomerService service;


    @GetMapping(path = "{id}")
    @ResponseStatus(OK)
    @ApiOperation("Search for a customer information by id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class),
            @ApiResponse(code = 404, message = "Not found", response = ApiError.class)
    })
    public CustomerResponseWithoutOrdersDTO findWithoutOrders(
            @PathVariable @ApiParam(value = "Customer id to search for", required = true) Long id) {

        CustomerResponseWithoutOrdersDTO customer = service.findWithoutOrders(id);
        return customer;
    }

    @GetMapping(path = "{id}/orders")
    @ResponseStatus(OK)
    @ApiOperation("Search for a customer with their orders by id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class),
            @ApiResponse(code = 404, message = "Not found", response = ApiError.class)
    })
    public CustomerResponseWithOrdersDTO findWithOrders(
            @PathVariable @ApiParam(value = "Customer id to search for", required = true) Long id) {

        CustomerResponseWithOrdersDTO customer = service.findWithOrders(id);
        return customer;
    }

    @GetMapping
    @ResponseStatus(OK)
    @ApiOperation("Filter customers by fields")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class)
    })
    public List<CustomerResponseWithoutOrdersDTO> filter(CustomerRequestDTO filter) {
        Customer customer = filter.toEntity();
        List<Customer> list = service.filter(customer);

        return list.stream().map(x -> x.withoutOrdersDTO()).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation(value = "Create a customer")
    @ApiResponses({
        @ApiResponse(
            code = 201,
            message = "Created",
            responseHeaders = {
                @ResponseHeader(
                    name = "location",
                    description = "The URI to the created customer",
                    response = URI.class
                )
            }
        ),
        @ApiResponse(code = 400, message = "Bad request", response = ApiError.class)
    })
    public ResponseEntity<CustomerResponseWithoutOrdersDTO> save(
            @RequestBody @Valid @ApiParam(value = "Customer information", required = true) CustomerRequestDTO customer) {

        Customer entity = customer.toEntity();
        entity = service.save(entity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entity.getId()).toUri();
        return ResponseEntity.created(uri).body(entity.withoutOrdersDTO());
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation(value = "Delete a customer by id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class),
            @ApiResponse(code = 404, message = "Not found", response = ApiError.class)
    })
    public void delete(@PathVariable @ApiParam(value = "Customer id to delete", required = true) Long id) {
        service.delete(id);
    }

    @PutMapping(path = "{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation(value = "Update a customer by id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class),
            @ApiResponse(code = 404, message = "Not found", response = ApiError.class)
    })
    public void update(@PathVariable @ApiParam(value = "Customer id to update", required = true) Long id,
                       @RequestBody @ApiParam(value = "Customer new information", required = true)
                               CustomerRequestDTO customer) {

        Customer entity = customer.toEntity();
        service.update(id, entity);
    }

}
