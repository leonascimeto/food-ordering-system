package com.food.ordering.system.domain.entity;

import com.food.ordering.system.domain.valueObject.RestaurantId;

public abstract class AggregateRoot<ID> extends BaseEntity<ID> {
    public AggregateRoot(RestaurantId restaurantId) {
        super();
    }
}
