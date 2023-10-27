package com.company.restaurantsystem.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "COOK_ORDER_REQUEST", indexes = {
        @Index(name = "IDX_COOK_ORDER_REQUEST_RESTAURANT", columnList = "RESTAURANT_ID")
})
@Entity
@Getter
@Setter
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

    @OneToMany(mappedBy = "cookRequestDTO")
    private List<CookFoodItemEntity> cookingItems;

    @JoinColumn(name = "RESTAURANT_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

}