package dev.alexandrevieira.sales.api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.alexandrevieira.sales.domain.entities.Order;
import dev.alexandrevieira.sales.domain.entities.OrderItem;
import dev.alexandrevieira.sales.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class OrderResponseDTO implements Serializable {
    private Long id;
    private LocalDate orderDate;
    private List<OrderItemDTO> orderItems;
    private OrderStatus status;

    //@JsonIdentityInfo( generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    //@JsonIdentityReference(alwaysAsId = true)
    @ToString.Exclude
    private Long customer;

    public OrderResponseDTO() {
    }

    public OrderResponseDTO(Order order){
        this.id = order.getId();
        this.orderDate = order.getOrderDate();
        this.customer = order.getCustomer().getId();
        this.orderItems = new ArrayList<>();
        this.status = order.getStatus();
        order.getOrderItems().forEach(x -> this.orderItems.add(new OrderItemDTO(x)));
    }


    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.valueOf(0);

        for (OrderItemDTO item : orderItems) {
            //adding (quantity * priceAtOrder) to total
            total = total.add(item.getPriceAtOrder().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        return total;
    }

    @JsonIgnore
    public Order toEntity() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, Order.class);
    }

    @Getter
    @Setter
    @ToString
    public static class OrderItemDTO implements Serializable {
        private Long id;
        private ProductRequestDTO product;
        private Integer quantity;
        private BigDecimal priceAtOrder;

        public OrderItemDTO(OrderItem orderItem) {
            this.id = orderItem.getId();
            this.product = new ProductRequestDTO(orderItem.getProduct());
            this.quantity = orderItem.getQuantity();
            this.priceAtOrder = orderItem.getPriceAtOrder();
        }

    }
}
