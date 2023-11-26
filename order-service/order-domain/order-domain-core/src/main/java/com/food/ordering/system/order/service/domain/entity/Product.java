package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueObject.Money;
import com.food.ordering.system.domain.valueObject.ProductId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    public Product(ProductId productId) {
        super.setId(productId);
    }

    public void updateWithConfirmedNameAndPrice(String name, Money price) {
        this.name = name;
        this.price = price;
    }
}
