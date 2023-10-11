package com.company.restaurantapi.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.JmixEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JmixEntity(name = "rstaddn_RestaurantFoodItemDTO")
public class RestaurantFoodItemDTO {
    @JmixGeneratedValue
    @JmixId
    private UUID id;

    private String name;

    private byte[] icon;

    private Integer price;

    private String description;
}