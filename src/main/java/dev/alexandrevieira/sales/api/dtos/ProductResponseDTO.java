package dev.alexandrevieira.sales.api.dtos;

import dev.alexandrevieira.sales.domain.entities.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
//DTO class to respond Product
public class ProductResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String description;
    private BigDecimal price;

    public ProductResponseDTO() {
    }

    public ProductResponseDTO(Product product) {
        this.id = product.getId();
        this.description = product.getDescription();
        this.price = product.getPrice();
    }
}
