package com.company.orderapi.model;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@JmixEntity(name = "ordrapi_FoodItemCountedDTO")
public class FoodItemCountedDTO {
    @JmixGeneratedValue
    @JmixId
    private UUID id;

    private Integer count;

    @InstanceName
    private String foodItemName;

    private UUID foodItemOriginalId;
}