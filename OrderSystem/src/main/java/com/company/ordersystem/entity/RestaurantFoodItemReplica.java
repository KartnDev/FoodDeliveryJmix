package com.company.ordersystem.entity;

import com.company.restaurantapi.model.HasIcon;
import com.company.useroidcplagin.entity.AppUser;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.util.UUID;

@JmixEntity
@Table(name = "RESTAURANT_FOOD_ITEM_REPLICA")
@Entity
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSourceId() {
        return sourceId;
    }

    public void setSourceId(UUID sourceId) {
        this.sourceId = sourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public byte[] getIcon() {
        return icon;
    }

    @Override
    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AppUser getBelongsToUser() {
        return belongsToUser;
    }

    public void setBelongsToUser(AppUser belongsToUser) {
        this.belongsToUser = belongsToUser;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public FoodItemCountedEntity getFoodItemCountedEntity() {
        return foodItemCountedEntity;
    }

    public void setFoodItemCountedEntity(FoodItemCountedEntity foodItemCountedEntity) {
        this.foodItemCountedEntity = foodItemCountedEntity;
    }
}