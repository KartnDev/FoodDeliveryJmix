package com.company.restaurantsystem.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.util.UUID;

@JmixEntity
@Table(name = "COOK_FOOD_ITEM_ENTITY", indexes = {
        @Index(name = "IDX_COOK_FOOD_ITEM_ENTITY_FOOD_TO_COOK", columnList = "FOOD_TO_COOK_ID"),
        @Index(name = "IDX_COOK_FOOD_ITEM_ENTITY_COOK_REQUEST_DTO", columnList = "COOK_REQUEST_DTO_ID")
})
@Entity
public class CookFoodItemEntity {
    @InstanceName
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @JoinColumn(name = "FOOD_TO_COOK_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private RestaurantFoodItem foodToCook;

    @Column(name = "COUNT_")
    private Integer count;

    @JoinColumn(name = "COOK_REQUEST_DTO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private CookOrderRequest cookOrderRequest;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RestaurantFoodItem getFoodToCook() {
        return foodToCook;
    }

    public void setFoodToCook(RestaurantFoodItem foodToCook) {
        this.foodToCook = foodToCook;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public CookOrderRequest getCookOrderRequest() {
        return cookOrderRequest;
    }

    public void setCookOrderRequest(CookOrderRequest cookOrderRequest) {
        this.cookOrderRequest = cookOrderRequest;
    }
}