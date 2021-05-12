package dev.alexandrevieira.sales.domain.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.alexandrevieira.sales.api.dtos.OrderRequestDTO;
import dev.alexandrevieira.sales.api.dtos.OrderResponseDTO;
import dev.alexandrevieira.sales.domain.enums.OrderStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "`order`")
@Slf4j
public class Order implements Serializable, GenericEntity<Long> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore @ToString.Exclude
    private Customer customer;

    @Column(nullable = false)
    private LocalDate orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order() {
    }

    public Order(OrderRequestDTO dto) {
        this.id = dto.getId();
        this.orderDate = dto.getDate() == null ? null : LocalDate.parse(dto.getDate());
        this.status = dto.getStatus();
    }

    public BigDecimal getTotal() {
        log.debug(this.getClass().getSimpleName() + ".getTotal()");
        BigDecimal total = BigDecimal.valueOf(0);

        for(OrderItem item : orderItems) {
            total = total.add(item.getPriceAtOrder().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        return total;
    }

    @Override
    @JsonIgnore
    public boolean allFieldsAreNullOrEmpty() {
        log.debug(this.getClass().getSimpleName() + ".allFieldsAreNullOrEmpty()");
        return id == null && orderDate == null && status == null;
    }

    @Override
    public OrderResponseDTO toDTO() {
        log.info(this.getClass().getSimpleName() + ".toDTO()");
        return new OrderResponseDTO(this);
    }
}
