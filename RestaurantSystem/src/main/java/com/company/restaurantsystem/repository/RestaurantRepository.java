package com.company.restaurantsystem.repository;

import com.company.restaurantsystem.entity.Restaurant;
import io.jmix.core.repository.JmixDataRepository;

import java.util.UUID;

public interface RestaurantRepository extends JmixDataRepository<Restaurant, Long> {
}
