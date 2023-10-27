package com.company.ordersystem.service;

import com.company.orderapi.model.FoodItemCountedDTO;
import com.company.orderapi.model.OrderDTO;
import com.company.ordersystem.entity.FoodItemCountedEntity;
import com.company.ordersystem.entity.Order;
import io.jmix.core.DataManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final DataManager dataManager;

    public OrderDTO convert(Order order) {
        var orderDTO = dataManager.create(OrderDTO.class);
        orderDTO.setOriginOrderId(order.getId());
        orderDTO.setRestaurantId(orderDTO.getRestaurantId());
        orderDTO.setRestaurantName(order.getRestaurantName());
        orderDTO.setItems(createOrderDTOItems(order.getOrderItems()));
        return orderDTO;
    }

    private List<FoodItemCountedDTO> createOrderDTOItems(List<FoodItemCountedEntity> orderItems) {
        var list = new ArrayList<FoodItemCountedDTO>();
        orderItems.forEach(item -> {
            var foodItemCountedDTO = dataManager.create(FoodItemCountedDTO.class);
            foodItemCountedDTO.setCount(item.getCount());
            foodItemCountedDTO.setFoodItemOriginalId(item.getItem().getSourceId());
            foodItemCountedDTO.setFoodItemName(item.getItem().getName());
            list.add(foodItemCountedDTO);
        });
        return list;
    }
}
