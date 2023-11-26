package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueObject.*;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.valueObject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueObject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueObject.TrackingId;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@EqualsAndHashCode(callSuper = true)
public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    public void initializeOrder() {
        this.setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    public void pay(){
        if(orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException("Order is not in correct state for payment!");
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approve(){
        if(orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for approval!");
        }
        orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessages){
        if(orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for init cancellation!");
        }
        orderStatus = OrderStatus.CANCELLING;
        this.updateFailureMessages(failureMessages);
    }

    public void cancel(List<String> failureMessages){
        if(!(orderStatus == OrderStatus.CANCELLING || orderStatus == OrderStatus.PENDING)) {
            throw new OrderDomainException("Order is not in correct state for cancel operation!");
        }
        orderStatus = OrderStatus.CANCELLED;
        this.updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if(this.failureMessages != null && failureMessages != null){
            this.failureMessages.addAll(failureMessages.stream().filter(message -> !message.isEmpty()).toList());
        }

        if(this.failureMessages == null){
            this.failureMessages = failureMessages;
        }
    }


    private void validateItemsPrice() {
        Money orderItemsTotal = this.items.stream().map(orderItem -> {
            validateItemPrice(orderItem);
            return orderItem.getSubtotal();
        }).reduce(Money.ZERO, Money::add);

        if(!price.equals(orderItemsTotal)) {
            throw new OrderDomainException("Total price " + price.getAmount() + " does not match items total " + orderItemsTotal.getAmount() + "!");
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if(!orderItem.isPriceValid()) {
            throw new OrderDomainException("Order item price: " + orderItem.getPrice().getAmount() + " is not valid for product "
                    + orderItem.getProduct().getId().getValue());
        }

    }

    private void validateTotalPrice() {
        if(this.price == null || !this.price.isGreaterThanZero()) {
            throw new OrderDomainException("Order total price must be greater than zero!");
        }
    }

    private void validateInitialOrder() {
        if(orderStatus != null || this.getId() != null) {
            throw new OrderDomainException("Order is not in correct state for initialization!");
        }
    }

    private void initializeOrderItems() {
        long itemId = 1;
        for (OrderItem orderItem : items) {
            orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }
}
