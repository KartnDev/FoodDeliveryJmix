package com.company.restaurantsystem.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "COOK_ORDER_REQUEST", indexes = {
        @Index(name = "IDX_COOK_ORDER_REQUEST_RESTAURANT", columnList = "RESTAURANT_ID")
})
@Entity
public class CookOrderRequest {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @InstanceName
    @Column(name = "ORDER_ID")
    private Long orderId;

    @Column(name = "IS_DONE")
    private Boolean isDone;

    @OneToMany(mappedBy = "cookOrderRequest")
    private List<CookFoodItemEntity> cookingItems;

    @JoinColumn(name = "RESTAURANT_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean done) {
        isDone = done;
    }

    public List<CookFoodItemEntity> getCookingItems() {
        return cookingItems;
    }

    public void setCookingItems(List<CookFoodItemEntity> cookingItems) {
        this.cookingItems = cookingItems;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}