package dev.alexandrevieira.sales.api.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusUpdateDTO {
    private String newStatus;
}
