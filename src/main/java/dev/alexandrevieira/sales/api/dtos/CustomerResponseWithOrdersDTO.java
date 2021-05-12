package dev.alexandrevieira.sales.api.dtos;

import dev.alexandrevieira.sales.domain.entities.Customer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class CustomerResponseWithOrdersDTO implements Serializable {
    private Long id;
    private String name;
    private String cpf;
    private List<OrderResponseDTO> orders = new ArrayList<>();

    public CustomerResponseWithOrdersDTO() {
    }

    public CustomerResponseWithOrdersDTO(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.cpf = customer.getCpf();
        customer.getOrders().forEach(x -> orders.add(new OrderResponseDTO(x)));
    }
}
