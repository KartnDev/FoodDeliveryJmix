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

    @Getter
    @InstanceName
    private String name;

    private byte[] icon;

    @Getter
    private Integer price;

    @Getter
    private String description;

    @Override
    public String getAttachmentName() {
        return name + "_item_icon.png";
    }


    public void setId(UUID id) {
        this.id = id;
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

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}