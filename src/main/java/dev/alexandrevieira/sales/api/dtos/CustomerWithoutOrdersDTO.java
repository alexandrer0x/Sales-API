package dev.alexandrevieira.sales.api.dtos;

import dev.alexandrevieira.sales.domain.entities.Customer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class CustomerWithoutOrdersDTO implements Serializable {
    private Long id;
    private String name;
    private String cpf;

    public CustomerWithoutOrdersDTO() {
    }

    public CustomerWithoutOrdersDTO(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.cpf = customer.getCpf();
    }
}
