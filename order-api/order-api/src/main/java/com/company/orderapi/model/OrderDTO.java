package com.company.orderapi.model;


import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@JmixEntity(name = "ordrapi_OrderDTO")
public class OrderDTO {
    @JmixGeneratedValue
    @JmixId
    private UUID id;

    private Long originOrderId;
    private Long restaurantId;
    private List<FoodItemCountedDTO> items;
    private String restaurantName;

    @InstanceName
    private String orderName() {
        return "Order from " + restaurantName + " #" + id;
    }
}
