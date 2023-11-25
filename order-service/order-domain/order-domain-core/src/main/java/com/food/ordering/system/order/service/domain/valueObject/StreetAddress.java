package com.food.ordering.system.order.service.domain.valueObject;

import lombok.*;

import java.util.UUID;

@Data
public class StreetAddress {
    private final UUID id;
    private final String street;
    private final String postalCode;
    private final String city;
}
