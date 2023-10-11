package com.company.restaurantsystem.repository;

import com.company.restaurantsystem.entity.RestaurantMenu;
import io.jmix.core.FetchPlan;
import io.jmix.core.repository.JmixDataRepository;

import java.util.List;
import java.util.UUID;

public interface RestaurantMenuRepository extends JmixDataRepository<RestaurantMenu, UUID> {

    List<RestaurantMenu> findRestaurantMenuByRestaurantId(Long restaurantId, FetchPlan fetchPlan);
}
