package dev.alexandrevieira.sales.domain.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.alexandrevieira.sales.api.dtos.ProductDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Slf4j
public class Product implements Serializable, GenericEntity<Long> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(precision = 15, scale = 2, nullable = false, columnDefinition = "decimal(15, 2) unsigned")
    private BigDecimal price;

    @Override
    @JsonIgnore
    public boolean allFieldsAreNullOrEmpty() {
        return id == null && (description == null || description.isEmpty()) && (price == null);
    }

    @Override
    public ProductDTO toDTO() {
        log.info(this.getClass().getSimpleName() + ".toDTO()");
        return new ProductDTO(this);
    }
}
