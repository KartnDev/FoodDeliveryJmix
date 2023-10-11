package com.company.restaurantsystem.api.rest.v1;

import com.company.restaurantapi.entity.RestaurantDTO;
import com.company.restaurantapi.entity.RestaurantFoodItemDTO;
import com.company.restaurantapi.entity.RestaurantMenuDTO;
import com.company.restaurantsystem.entity.RestaurantFoodItem;
import com.company.restaurantsystem.entity.RestaurantMenu;
import com.company.restaurantsystem.repository.RestaurantMenuRepository;
import com.company.restaurantsystem.service.AttachmentService;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlans;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.StreamSupport;

@Secured("system-full-access")
@RestController
@AllArgsConstructor
@RequestMapping(value = "api/v1")
public class RestaurantMenuController {

    private final RestaurantMenuRepository restaurantMenuRepository;
    private final FetchPlans fetchPlans;
    private final DataManager dataManager;
    private final AttachmentService attachmentService;
    @GetMapping("/restaurant/{restaurantId}/menus")
    public List<RestaurantMenuDTO> listRestaurants(@PathVariable Long restaurantId) {
        var fetchPlan = fetchPlans.builder(RestaurantMenu.class)
                .addFetchPlan("restaurantMenu-with-items-fetch-plan")
                .build();
        var restaurantMenuSpliterator = restaurantMenuRepository.findRestaurantMenuByRestaurantId(restaurantId, fetchPlan)
                .spliterator();
        return StreamSupport.stream(restaurantMenuSpliterator, false)
                .map(menu -> {
                    var menuDTO = dataManager.create(RestaurantMenuDTO.class);
                    menuDTO.setId(menu.getId());
                    menuDTO.setName(menu.getName());
                    menuDTO.setItems(convertItemsToDTO(menu.getItems()));
                    return menuDTO;
                })
                .toList();
    }

    private List<RestaurantFoodItemDTO> convertItemsToDTO(List<RestaurantFoodItem> items) {
        return items.stream()
                .map(item -> {
                    var itemDTO = dataManager.create(RestaurantFoodItemDTO.class);
                    itemDTO.setId(item.getId());
                    itemDTO.setName(item.getName());
                    itemDTO.setDescription(item.getDescription());
                    itemDTO.setPrice(item.getPrice());
                    itemDTO.setIcon(attachmentService.getAttachmentAsByteArray(item));
                    return itemDTO;
                })
                .toList();
    }
}
