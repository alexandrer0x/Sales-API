package dev.alexandrevieira.sales.api.resources;

import dev.alexandrevieira.sales.api.dtos.NewOrderDTO;
import dev.alexandrevieira.sales.api.dtos.OrderRequestDTO;
import dev.alexandrevieira.sales.api.dtos.OrderResponseDTO;
import dev.alexandrevieira.sales.api.dtos.OrderStatusUpdateDTO;
import dev.alexandrevieira.sales.api.exception.ApiError;
import dev.alexandrevieira.sales.domain.entities.Order;
import dev.alexandrevieira.sales.domain.enums.OrderStatus;
import dev.alexandrevieira.sales.services.OrderService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping(path = "orders", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class OrderResource {
    @Autowired
    private OrderService service;

    @GetMapping(path = "{id}")
    @ResponseStatus(OK)
    @ApiOperation("Search for a order information by id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class),
            @ApiResponse(code = 404, message = "Not found", response = ApiError.class)
    })
    public OrderResponseDTO find(@PathVariable @ApiParam(value = "Product id to search for", required = true) Long id) {
        log.info(this.getClass().getSimpleName() + ".find(@PathVariable Long id)");
        return service.findDTO(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    @ApiOperation("Filter orders by fields")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class)
    })
    public List<OrderResponseDTO> filter(OrderRequestDTO filter) {
        Order order = filter.toEntity();
        List<Order> list = service.filter(order);

        return list.stream().map(x -> x.toDTO()).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation(value = "Create a order")
    @ApiResponses({
            @ApiResponse(
                    code = 201,
                    message = "Created",
                    responseHeaders = {
                            @ResponseHeader(
                                    name = "location",
                                    description = "The URI to the created order",
                                    response = URI.class
                            )
                    }
            ),
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class)
    })
    public ResponseEntity<OrderResponseDTO> save(@RequestBody @Valid @ApiParam(value = "Order information", name = "order", required = true) NewOrderDTO dto) {
        Order order = service.saveDTO(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(order.getId()).toUri();
        return ResponseEntity.created(uri).body(order.toDTO());
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation(value = "Delete a order by id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class),
            @ApiResponse(code = 404, message = "Not found", response = ApiError.class)
    })
    public void delete(@PathVariable @ApiParam(value = "Order id to delete", required = true) Long id) {
        service.delete(id);
    }

    @PatchMapping(path = "{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation(value = "Update order status by id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class),
            @ApiResponse(code = 404, message = "Not found", response = ApiError.class)
    })
    public void updateOrderStatus(@PathVariable @ApiParam(value = "Order id to update", required = true) Long id,
                             @RequestBody @ApiParam(value = "Order new status", required = true) OrderStatusUpdateDTO orderStatusUpdateDTO) {
        OrderStatus status = OrderStatus.valueOf(orderStatusUpdateDTO.getNewStatus());
        service.updateOrderStatus(id, status);
    }
}
