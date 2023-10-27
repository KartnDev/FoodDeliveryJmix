package com.company.ordersystem.entity;

import com.company.restaurantapi.model.HasIcon;
import com.company.useroidcplagin.entity.AppUser;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@JmixEntity
@Table(name = "RESTAURANT_FOOD_ITEM_REPLICA")
@Entity
@Getter
@Setter
public class RestaurantFoodItemReplica implements HasIcon {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "SOURCE_ID")
    private UUID sourceId;

    @InstanceName
    @Column(name = "NAME")
    private String name;

    @Column(name = "ICON")
    private byte[] icon;

    @Column(name = "DESCRIPTION")
    private String description;

    @JoinColumn(name = "BELONGS_TO_USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser belongsToUser;

    @Column(name = "PRICE")
    private Integer price;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "item")
    private FoodItemCountedEntity foodItemCountedEntity;

    @Override
    public String getAttachmentName() {
        return name + "_item_icon.png";
    }
}