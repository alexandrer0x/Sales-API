package dev.alexandrevieira.sales.api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.alexandrevieira.sales.domain.entities.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ProductRequestDTO implements Serializable {
    @JsonIgnore
    private Long id;

    @NotEmpty(message = "Description field is empty")
    @Length(max = 255)
    private String description;

    @NotNull(message = "Invalid price")
    @DecimalMin(value = "0.01", message = "Minimum price is 0.01")
    private BigDecimal price;

    public ProductRequestDTO() {
    }

    public ProductRequestDTO(Product product) {
        this.id = product.getId();
        this.description = product.getDescription();
        this.price = product.getPrice();
    }

    public Product toEntity() {
        return new Product(this);
    }
}
