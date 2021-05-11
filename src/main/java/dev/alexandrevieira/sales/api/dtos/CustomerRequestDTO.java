package dev.alexandrevieira.sales.api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.alexandrevieira.sales.domain.entities.Customer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class CustomerRequestDTO implements Serializable {
    @JsonIgnore
    private Long id;

    @NotEmpty(message = "Name field is empty")
    @Length(max = 100, message = "Name max length is 100 characters")
    private String name;

    @NotEmpty(message = "CPF field is empty")
    @CPF(message = "Invalid CPF number")
    @Length(min = 11, max = 11, message = "Invalid CPF number")
    private String cpf;

    public CustomerRequestDTO() {
    }

    @JsonIgnore
    public Customer toEntity() {
        return new Customer(this);
    }
}
