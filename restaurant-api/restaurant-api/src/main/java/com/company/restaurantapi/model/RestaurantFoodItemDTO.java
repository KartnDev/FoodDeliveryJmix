package com.company.restaurantapi.model;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@JmixEntity(name = "rstaddn_RestaurantFoodItemDTO")
public class RestaurantFoodItemDTO implements HasIcon {
    @Getter
    @JmixGeneratedValue
    @JmixId
    private UUID id;

    @InstanceName
    private String name;

    private byte[] icon;

    private Integer price;

    private String description;

    @Override
    public String getAttachmentName() {
        return name + "_item_icon.png";
    }

    @Override
    public byte[] getIcon() {
        return icon;
    }

    @Override
    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}