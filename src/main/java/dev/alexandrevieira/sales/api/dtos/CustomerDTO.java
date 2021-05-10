package dev.alexandrevieira.sales.api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.alexandrevieira.sales.domain.entities.Customer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class CustomerDTO implements Serializable {
    private Long id;

    @NotEmpty(message = "Name field is empty")
    @Length(max = 100, message = "Name max length is 100 characters")
    private String name;

    @NotEmpty(message = "CPF field is empty")
    @CPF(message = "Invalid CPF number")
    @Length(min = 11, max = 11, message = "Invalid CPF number")
    private String cpf;

    private List<OrderDTO> orders = new ArrayList<>();

    public CustomerDTO() {
    }

    public CustomerDTO(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.cpf = customer.getCpf();
        customer.getOrders().forEach(x -> orders.add(new OrderDTO(x)));
    }

    @JsonIgnore
    public Customer toEntity() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, Customer.class);
    }
}
