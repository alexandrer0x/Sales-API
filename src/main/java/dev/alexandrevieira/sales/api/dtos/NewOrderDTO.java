package dev.alexandrevieira.sales.api.dtos;

import dev.alexandrevieira.sales.validation.NotEmptyList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
//Model class to receive new orders
public class NewOrderDTO {

    @NotNull(message = "Invalid customer id")
    @Min(value = 1, message = "Invalid customer id")
    private Long customer;

    @NotEmptyList(message = "Not allowed creating a order without items")
    private List<ItemDTO> orderItems = new ArrayList<>();

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.valueOf(0);

        for (ItemDTO item : orderItems) {
            total = total.add(item.getPriceAtOrder().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        return total;
    }


    @Getter
    @Setter
    @ToString
    public static class ItemDTO {
        private Long product;
        private Integer quantity;
        private BigDecimal priceAtOrder;
    }
}
