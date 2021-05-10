package dev.alexandrevieira.sales.api.resources;

import dev.alexandrevieira.sales.api.dtos.NewOrderDTO;
import dev.alexandrevieira.sales.api.dtos.OrderDTO;
import dev.alexandrevieira.sales.api.dtos.OrderStatusUpdateDTO;
import dev.alexandrevieira.sales.domain.entities.Order;
import dev.alexandrevieira.sales.domain.enums.OrderStatus;
import dev.alexandrevieira.sales.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "orders")
@Slf4j
public class OrderResource {
    @Autowired
    private OrderService service;

    @GetMapping(path = "{id}")
    @ResponseStatus(OK)
    public OrderDTO find(@PathVariable Long id) {
        log.info(this.getClass().getSimpleName() + ".find(@PathVariable Long id)");
        return service.findDTO(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<OrderDTO> filter(OrderDTO filter) {
        Order order = filter.toEntity();
        List<Order> list = service.filter(order);

        return list.stream().map(x -> x.toDTO()).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<OrderDTO> save(@RequestBody @Valid NewOrderDTO dto) {
        Order order = service.saveDTO(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(order.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PatchMapping(path = "{id}")
    public void updateOrderStatus(@PathVariable Long id,
                             @RequestBody OrderStatusUpdateDTO orderStatusUpdateDTO) {
        OrderStatus status = OrderStatus.valueOf(orderStatusUpdateDTO.getNewStatus());
        service.updateOrderStatus(id, status);
    }
}
