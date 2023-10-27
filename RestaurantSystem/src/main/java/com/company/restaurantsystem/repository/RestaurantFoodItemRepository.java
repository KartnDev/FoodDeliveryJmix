package com.company.restaurantsystem.repository;

import com.company.restaurantsystem.entity.RestaurantFoodItem;
import io.jmix.core.repository.JmixDataRepository;

import java.util.UUID;

public interface RestaurantFoodItemRepository extends JmixDataRepository<RestaurantFoodItem, UUID> {
}
