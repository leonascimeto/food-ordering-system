package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueObject.RestaurantId;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class Restaurant extends AggregateRoot<RestaurantId> {
    private final List<Product> products;
    private boolean active;

    private Restaurant(RestaurantBuilder builder) {
        super.setId(builder.restaurantId);
        this.products = builder.products;
        this.active = builder.active;
    }

    public static RestaurantBuilder builder() {
        return new RestaurantBuilder();
    }

    public List<Product> getProducts() {
        return products;
    }

    public boolean isActive() {
        return active;
    }

    public static final class RestaurantBuilder {
        private RestaurantId restaurantId;
        private List<Product> products;
        private boolean active;

        public RestaurantBuilder restaurantId(RestaurantId restaurantId) {
            this.restaurantId = restaurantId;
            return this;
        }

        public RestaurantBuilder products(List<Product> products) {
            this.products = products;
            return this;
        }

        public RestaurantBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }

}
