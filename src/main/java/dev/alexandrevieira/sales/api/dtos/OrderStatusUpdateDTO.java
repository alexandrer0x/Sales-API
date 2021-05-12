package dev.alexandrevieira.sales.api.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
//DTO class to receive updates to order status
public class OrderStatusUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String newStatus;
}
