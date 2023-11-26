package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class OrderCreateHelper {
    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        saveOrder(order);
        log.info("Order with id {} created", orderCreatedEvent.getOrder().getId().getValue());
        return orderCreatedEvent;
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper.createOrderComandToRestaurant(createOrderCommand);
        restaurantRepository.findRestaurantInformation(restaurant).orElseThrow(() -> {
            log.error("Restaurant with id {} not found", restaurant.getId());
            return new OrderDomainException("Restaurant with id "+ restaurant.getId() +" not found");
        });
        return restaurant;
    }

    private void checkCustomer(UUID customerId) {
        customerRepository.finsCustomer(customerId).orElseThrow(() -> {
            log.error("Customer with id {} not found", customerId);
            return new OrderDomainException("Customer with id "+ customerId +" not found");
        });
    }

    private Order saveOrder(Order order) {
        Order orderResult =  orderRepository.save(order);
        if(orderResult == null) {
            log.error("Order with id {} not saved", order.getId());
            throw new OrderDomainException("Order with id "+ order.getId() +" not saved");
        }
        log.info("Order with id {} saved", order.getId());
        return orderResult;
    }
}
