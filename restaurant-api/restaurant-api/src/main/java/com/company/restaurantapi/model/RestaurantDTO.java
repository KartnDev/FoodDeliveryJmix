package com.company.restaurantapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import lombok.Getter;
import lombok.Setter;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String getAttachmentName() {
        return name + "_icon.png";
    }
}