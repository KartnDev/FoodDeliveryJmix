package com.company.restaurantapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JmixEntity(name = "rstaddn_RestaurantMenuDTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantMenuDTO {
    @JmixGeneratedValue
    @JmixId
    private Long id;

    @InstanceName
    private String name;

    private List<RestaurantFoodItemDTO> items;
}