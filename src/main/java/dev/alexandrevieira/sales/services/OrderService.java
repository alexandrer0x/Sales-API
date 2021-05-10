package dev.alexandrevieira.sales.services;

import dev.alexandrevieira.sales.api.dtos.NewOrderDTO;
import dev.alexandrevieira.sales.api.dtos.OrderDTO;
import dev.alexandrevieira.sales.domain.entities.Customer;
import dev.alexandrevieira.sales.domain.entities.Order;
import dev.alexandrevieira.sales.domain.entities.OrderItem;
import dev.alexandrevieira.sales.domain.entities.Product;
import dev.alexandrevieira.sales.domain.enums.OrderStatus;
import dev.alexandrevieira.sales.domain.repositories.CustomerRepository;
import dev.alexandrevieira.sales.domain.repositories.OrderItemRepository;
import dev.alexandrevieira.sales.domain.repositories.OrderRepository;
import dev.alexandrevieira.sales.domain.repositories.ProductRepository;
import dev.alexandrevieira.sales.exceptions.BusinessRuleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService extends GenericEntityService<Order, Long, OrderRepository> {
    @Autowired
    private OrderRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository) {
        super(orderRepository);
    }


    public OrderDTO findDTO(Long id) {
        Order order = ((OrderRepository) repository).findOneById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return order.toDTO();

    }

    @Transactional
    public Order saveDTO(NewOrderDTO newOrderDTO) {
        log.info(this.getClass().getSimpleName() + ".saveDTO(NewOrderDTO newOrderDTO)");
        List<Long> idList = new ArrayList<>();
        List<NewOrderDTO.ItemDTO> dtoItems = newOrderDTO.getOrderItems();
        Set<OrderItem> orderItems;
        Optional<Customer> customer;
        Order order;

        //creating a list containing the products ids
        log.debug("Adding product ids to the list");
        for(NewOrderDTO.ItemDTO item : newOrderDTO.getOrderItems()) {
            idList.add(item.getProduct());
        }

        //converting the List to Set, to test if there are duplicated ids
        log.debug("Converting ids List to Set");
        Set<Long> idSet = idList.stream().collect(Collectors.toSet());

        //if the List bigger than the Set then there are duplicated ids
        if(idSet.size() != idList.size()) {
            String message = "There are duplicated product ids";
            log.warn(message);
            throw new BusinessRuleException(message);
        }

        //searching the products in repository
        log.debug("Searching for products with the informed ids");
        List<Product> products = productRepository.findAllById(idList);

        //if the List returned by repository is smaller than id List then some product doesn't exist
        if(products.size() != idList.size()) {
            String message = "Not all product ids exist";
            log.warn(message);
            throw new BusinessRuleException(message);
        }

        //searching the customer in repository
        log.debug("Searching customer by id");
        customer  = customerRepository.findOneById(newOrderDTO.getCustomer());

        //if returned optional is empty then Customer doesn't exist
        if(!customer.isPresent()) {
            String message = "Customer id doesn't exist";
            log.warn(message);
            throw new BusinessRuleException(message);
        }

        //instantiating the Order
        order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.AWAITING_PAYMENT);
        order.setCustomer(customer.get());

        //inserting the order in repository
        log.debug("Inserting order");
        order = repository.save(order);

        //convert the DTO items to OrderItem objects
        orderItems = convertItems(dtoItems, order, products);
        order.setOrderItems(orderItems);

        //inserting order items in repository
        log.debug("Updating order items");
        orderItemRepository.saveAll(orderItems);

        return order;
    }

    private Set<OrderItem> convertItems(List<NewOrderDTO.ItemDTO> items, Order order, List<Product> products) {
        log.info(this.getClass().getSimpleName()
                + ".convertItems(List<ItemDTO> items, Order order, List<Product> products)");

        if(items.isEmpty()) {
            throw new BusinessRuleException("Not allowed creating a order without items");
        }

        //convert the products List to Map
        Map<Long, Product> productsMap = products.stream().collect(Collectors.toMap(
                Product::getId, product -> product));

        Set<OrderItem> orderItems = new LinkedHashSet<>();

        //instantiating order items and adding to the List
        log.debug("Creating order items");
        for(NewOrderDTO.ItemDTO itemDTO : items) {
            OrderItem orderItem = new OrderItem();
            Long productId = itemDTO.getProduct();
            Product product = productsMap.get(productId);

            orderItem.setProduct(product);
            orderItem.setPriceAtOrder(product.getPrice());
            orderItem.setOrder(order);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItems.add(orderItem);
        }

        return orderItems;
    }

    @Transactional
    public void updateOrderStatus(Long id, OrderStatus status) {
        Order order = this.find(id);
        order.setStatus(status);
        repository.save(order);
    }
}
