package dev.alexandrevieira.sales.api.dtos;

import dev.alexandrevieira.sales.domain.entities.Customer;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@ApiModel
//DTO class to respond Customer without orders
public class CustomerResponseWithoutOrdersDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String cpf;

    public CustomerResponseWithoutOrdersDTO() {
    }

    public CustomerResponseWithoutOrdersDTO(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.cpf = customer.getCpf();
    }
}
