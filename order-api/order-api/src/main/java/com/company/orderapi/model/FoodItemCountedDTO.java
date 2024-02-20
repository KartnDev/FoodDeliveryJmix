package com.company.orderapi.model;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;
@JmixEntity(name = "ordrapi_FoodItemCountedDTO")
public class FoodItemCountedDTO {
    @JmixGeneratedValue
    @JmixId
    private UUID id;

    private Integer count;

    @InstanceName
    private String foodItemName;

    private UUID foodItemOriginalId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getFoodItemName() {
        return foodItemName;
    }

    public void setFoodItemName(String foodItemName) {
        this.foodItemName = foodItemName;
    }

    public UUID getFoodItemOriginalId() {
        return foodItemOriginalId;
    }

    public void setFoodItemOriginalId(UUID foodItemOriginalId) {
        this.foodItemOriginalId = foodItemOriginalId;
    }
}