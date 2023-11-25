package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueObject.Money;
import com.food.ordering.system.domain.valueObject.OrderId;
import com.food.ordering.system.order.service.domain.valueObject.OrderItemId;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderItem extends AggregateRoot<OrderItemId> {
    private OrderId orderId;
    private final Product product;
    private final int quantity;
    private final Money price;
    private final Money subtotal;

    boolean isPriceValid() {
        return price.isGreaterThanZero() && price.equals(product.getPrice()) && subtotal.equals(price.multiply(quantity));
    }

    void initializeOrderItem(OrderId orderId, OrderItemId orderItemId) {
        this.orderId = orderId;
        super.setId(orderItemId);
    }
}
