package dev.alexandrevieira.sales.domain.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.alexandrevieira.sales.api.dtos.CustomerRequestDTO;
import dev.alexandrevieira.sales.api.dtos.CustomerResponseWithOrdersDTO;
import dev.alexandrevieira.sales.api.dtos.CustomerResponseWithoutOrdersDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity

@NamedEntityGraph(
    name = "fetchOrders",
    attributeNodes = {
        @NamedAttributeNode("id"),
        @NamedAttributeNode("name"),
        @NamedAttributeNode("cpf"),
        @NamedAttributeNode(value = "orders", subgraph = "orderItems")
    },
    subgraphs = {
        @NamedSubgraph(
            name = "orderItems",
            attributeNodes = {
                @NamedAttributeNode(value = "orderItems", subgraph = "product")
            }
        ),
        @NamedSubgraph(
            name = "product",
            attributeNodes = {
                @NamedAttributeNode(value = "product")
            }
        )
    }
)

public class Customer implements Serializable, GenericEntity<Long> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 11, nullable = false, unique = true)
    private String cpf;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    public Customer() {
    }
    public Customer(CustomerRequestDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.cpf = dto.getCpf();
    }

    @JsonIgnore
    public boolean allFieldsAreNullOrEmpty() {
        return id == null && (name == null || name.isEmpty()) && (cpf == null || cpf.isEmpty()) && (this.orders == null || this.orders.isEmpty());
    }

    @JsonIgnore
    public CustomerResponseWithOrdersDTO toDTO() {
        log.info(this.getClass().getSimpleName() + ".toDTO()");
        return new CustomerResponseWithOrdersDTO(this);
    }

    public CustomerResponseWithoutOrdersDTO withoutOrdersDTO() {
        log.info(this.getClass().getSimpleName() + ".noOrdersDTO()");
        return new CustomerResponseWithoutOrdersDTO(this);
    }
}
