package com.company.restaurantsystem.service;

import com.company.orderapi.model.OrderDTO;
import com.company.restaurantsystem.entity.CookFoodItemEntity;
import com.company.restaurantsystem.entity.CookOrderRequest;
import com.company.restaurantsystem.repository.RestaurantFoodItemRepository;
import com.company.restaurantsystem.repository.RestaurantRepository;
import io.jmix.core.DataManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CookOrderService {
    private final DataManager dataManager;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantFoodItemRepository restaurantFoodItemRepository;


    public void submitNewCookOrderFromDTO(OrderDTO orderDTO) {
        var cookOrderRequest = dataManager.create(CookOrderRequest.class);
        cookOrderRequest.setOrderId(orderDTO.getOriginOrderId());
        cookOrderRequest.setIsDone(false);
        cookOrderRequest.setRestaurant(restaurantRepository.getById(orderDTO.getRestaurantId()));
        cookOrderRequest.setCookingItems(createCookingListFromDTO(orderDTO));
        dataManager.save(cookOrderRequest);
    }

    private List<CookFoodItemEntity> createCookingListFromDTO(OrderDTO orderDTO) {
        List<CookFoodItemEntity> list = new ArrayList<>();
        orderDTO.getItems().forEach(item -> {
            var foodToCook = restaurantFoodItemRepository.getById(UUID.fromString(item.getFoodItemOriginalId()));

            var cookFoodItemEntity = dataManager.create(CookFoodItemEntity.class);
            cookFoodItemEntity.setFoodToCook(foodToCook);
            cookFoodItemEntity.setCount(item.getCount());
            list.add(cookFoodItemEntity);
        });
        return list;
    }
}
