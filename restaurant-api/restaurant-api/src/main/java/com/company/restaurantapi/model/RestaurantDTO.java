package com.company.restaurantapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JmixEntity(name = "rstaddn_RestaurantDTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantDTO implements HasIcon {
    @JmixGeneratedValue
    @JmixId
    private Long id;

    @InstanceName
    private String name;

    private byte[] icon;

    private String description;

    @Override
    public String getAttachmentName() {
        return name + "_icon.png";
    }
}