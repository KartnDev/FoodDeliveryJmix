package com.company.restaurantsystem.role;

import com.company.restaurantsystem.entity.Restaurant;
import com.company.restaurantsystem.entity.RestaurantFoodItem;
import com.company.restaurantsystem.entity.RestaurantMenu;
import com.company.useroidcplagin.entity.AppUser;
import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;

@ResourceRole(name = "RestaurantAdministratorRole", code = RestaurantAdministratorRole.CODE)
public interface RestaurantAdministratorRole {
    String CODE = "restaurant-administrator";

    @EntityAttributePolicy(entityClass = AppUser.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = AppUser.class, actions = EntityPolicyAction.READ)
    void appUser();

    @EntityAttributePolicy(entityClass = Restaurant.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Restaurant.class, actions = EntityPolicyAction.ALL)
    void restaurant();

    @EntityAttributePolicy(entityClass = RestaurantFoodItem.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = RestaurantFoodItem.class, actions = EntityPolicyAction.ALL)
    void restaurantFoodItem();

    @EntityAttributePolicy(entityClass = RestaurantMenu.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = RestaurantMenu.class, actions = EntityPolicyAction.ALL)
    void restaurantMenu();

}