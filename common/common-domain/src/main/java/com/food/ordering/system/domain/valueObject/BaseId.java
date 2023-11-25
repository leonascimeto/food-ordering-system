package com.food.ordering.system.domain.valueObject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(of = "value")
public abstract class BaseId <T>{
    private final T value;
}
