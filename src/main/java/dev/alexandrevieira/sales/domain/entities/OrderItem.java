package dev.alexandrevieira.sales.domain.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.alexandrevieira.sales.api.dtos.OrderResponseDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Slf4j
public class OrderItem implements Serializable, GenericEntity<Long> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @EqualsAndHashCode.Include
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal priceAtOrder;

    @Override
    @JsonIgnore
    public boolean allFieldsAreNullOrEmpty() {
        log.debug(this.getClass().getSimpleName() + ".allFieldsAreNullOrEmpty()");
        return true;
    }

    @Override
    public OrderResponseDTO.OrderItemDTO toDTO() {
        log.info(this.getClass().getSimpleName() + ".toDTO()");
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, OrderResponseDTO.OrderItemDTO.class);
    }
}
