package com.company.orderapi.model;


import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.List;
import java.util.UUID;

@JmixEntity(name = "ordrapi_OrderDTO")
public class OrderDTO {
    @JmixGeneratedValue
    @JmixId
    private UUID id;

    private Long originOrderId;
    private Long restaurantId;
    private List<FoodItemCountedDTO> items;
    private String restaurantName;

    @InstanceName
    private String orderName() {
        return "Order from " + restaurantName + " #" + id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getOriginOrderId() {
        return originOrderId;
    }

    public void setOriginOrderId(Long originOrderId) {
        this.originOrderId = originOrderId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<FoodItemCountedDTO> getItems() {
        return items;
    }

    public void setItems(List<FoodItemCountedDTO> items) {
        this.items = items;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}
