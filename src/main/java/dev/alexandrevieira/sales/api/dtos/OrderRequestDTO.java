package dev.alexandrevieira.sales.api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.alexandrevieira.sales.domain.entities.Order;
import dev.alexandrevieira.sales.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
//DTO class to receive Order to filter operation
public class OrderRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String date;
    private OrderStatus status;

    public OrderRequestDTO() {
    }

    @JsonIgnore
    public Order toEntity() {
        return new Order(this);
    }

}
