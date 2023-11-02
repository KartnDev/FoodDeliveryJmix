package com.company.restaurantsystem.service;

import com.company.orderapi.model.OrderDTO;
import com.company.restaurantsystem.entity.CookFoodItemEntity;
import com.company.restaurantsystem.entity.CookOrderRequest;
import com.company.restaurantsystem.repository.RestaurantFoodItemRepository;
import com.company.restaurantsystem.repository.RestaurantRepository;
import io.jmix.core.DataManager;
import io.jmix.core.SaveContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CookOrderService {
    private final DataManager dataManager;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantFoodItemRepository restaurantFoodItemRepository;

    public CookOrderService(DataManager dataManager,
                            RestaurantRepository restaurantRepository,
                            RestaurantFoodItemRepository restaurantFoodItemRepository) {
        this.dataManager = dataManager;
        this.restaurantRepository = restaurantRepository;
        this.restaurantFoodItemRepository = restaurantFoodItemRepository;
    }


    public void submitNewCookOrderFromDTO(OrderDTO orderDTO) {
        var cookOrderRequest = dataManager.create(CookOrderRequest.class);
        cookOrderRequest.setOrderId(orderDTO.getOriginOrderId());
        cookOrderRequest.setIsDone(false);
        cookOrderRequest.setRestaurant(restaurantRepository.getById(orderDTO.getRestaurantId()));
        cookOrderRequest.setCookingItems(createCookingListFromDTO(cookOrderRequest, orderDTO));
        SaveContext saveContext = new SaveContext();
        saveContext.saving(cookOrderRequest);
        saveContext.saving(cookOrderRequest.getCookingItems());
        dataManager.save(saveContext);
    }

    private List<CookFoodItemEntity> createCookingListFromDTO(CookOrderRequest cookOrderRequest, OrderDTO orderDTO) {
        List<CookFoodItemEntity> list = new ArrayList<>();
        orderDTO.getItems().forEach(item -> {
            var foodToCook = restaurantFoodItemRepository.getById(item.getFoodItemOriginalId());

            var cookFoodItemEntity = dataManager.create(CookFoodItemEntity.class);
            cookFoodItemEntity.setFoodToCook(foodToCook);
            cookFoodItemEntity.setCount(item.getCount());
            cookFoodItemEntity.setCookOrderRequest(cookOrderRequest);
            list.add(cookFoodItemEntity);
        });
        return list;
    }
}
