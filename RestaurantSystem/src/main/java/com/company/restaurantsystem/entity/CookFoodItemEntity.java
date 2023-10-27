package com.company.restaurantsystem.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@JmixEntity
@Table(name = "COOK_FOOD_ITEM_ENTITY", indexes = {
        @Index(name = "IDX_COOK_FOOD_ITEM_ENTITY_FOOD_TO_COOK", columnList = "FOOD_TO_COOK_ID"),
        @Index(name = "IDX_COOK_FOOD_ITEM_ENTITY_COOK_REQUEST_DTO", columnList = "COOK_REQUEST_DTO_ID")
})
@Entity
@Getter
@Setter
public class CookFoodItemEntity {
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

}