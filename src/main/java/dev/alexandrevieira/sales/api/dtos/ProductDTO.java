package dev.alexandrevieira.sales.api.dtos;

import dev.alexandrevieira.sales.domain.entities.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ProductDTO implements Serializable {
    private Long id;

    @NotEmpty(message = "Description field is empty")
    @Length(max = 255)
    private String description;

    @NotNull(message = "Invalid price")
    @DecimalMin(value = "0.01", message = "Minimum price is 0.01")
    private BigDecimal price;

    public ProductDTO() {
    }

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.description = product.getDescription();
        this.price = product.getPrice();
    }

    public Product toEntity() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, Product.class);
    }
}
