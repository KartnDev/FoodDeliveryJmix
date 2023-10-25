package com.company.restaurantapi.model;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JmixEntity(name = "rstaddn_RestaurantFoodItemDTO")
public class RestaurantFoodItemDTO implements HasIcon {
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
}