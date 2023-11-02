package com.company.restaurantsystem.api.rest.v1;

import com.company.orderapi.model.OrderDTO;
import com.company.restaurantapi.model.RestaurantDTO;
import com.company.restaurantapi.model.RestaurantFoodItemDTO;
import com.company.restaurantapi.model.RestaurantMenuDTO;
import com.company.restaurantsystem.entity.Restaurant;
import com.company.restaurantsystem.entity.RestaurantFoodItem;
import com.company.restaurantsystem.entity.RestaurantMenu;
import com.company.restaurantsystem.repository.RestaurantMenuRepository;
import com.company.restaurantsystem.repository.RestaurantRepository;
import com.company.restaurantsystem.service.AttachmentService;
import com.company.restaurantsystem.service.CookOrderService;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlans;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import com.company.restaurantsystem.security.FullAccessRole;

import java.util.List;
import java.util.stream.StreamSupport;

@Secured(FullAccessRole.CODE)
@RestController
@RequestMapping(value = "api/v1")
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;
    private final DataManager dataManager;
    private final AttachmentService attachmentService;
    private final FetchPlans fetchPlans;
    private final RestaurantMenuRepository restaurantMenuRepository;
    private final CookOrderService cookOrderService;

    public RestaurantController(RestaurantRepository restaurantRepository,
                                DataManager dataManager,
                                AttachmentService attachmentService,
                                FetchPlans fetchPlans,
                                RestaurantMenuRepository restaurantMenuRepository,
                                CookOrderService cookOrderService) {
        this.restaurantRepository = restaurantRepository;
        this.dataManager = dataManager;
        this.attachmentService = attachmentService;
        this.fetchPlans = fetchPlans;
        this.restaurantMenuRepository = restaurantMenuRepository;
        this.cookOrderService = cookOrderService;
    }

    @GetMapping("/restaurants")
    public List<RestaurantDTO> listRestaurants() {
        return StreamSupport.stream(restaurantRepository.findAll().spliterator(), false)
                .map(restaurant -> {
                    var dto = dataManager.create(RestaurantDTO.class);
                    dto.setId(restaurant.getId());
                    dto.setName(restaurant.getName());
                    dto.setDescription(restaurant.getDescription());
                    dto.setIcon(attachmentService.getAttachmentAsByteArray(restaurant));
                    return dto;
                })
                .toList();
    }

    @GetMapping("/restaurants/{id}")
    public RestaurantDTO getRestaurant(@PathVariable Long id) {
        Restaurant restaurant = restaurantRepository.getById(id);
        var dto = dataManager.create(RestaurantDTO.class);
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setDescription(restaurant.getDescription());
        dto.setIcon(attachmentService.getAttachmentAsByteArray(restaurant));
        return dto;
    }

    @GetMapping("/restaurants/{restaurantId}/menus")
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

    @PostMapping("/restaurants/{restaurantId}/cook")
    public String getRestaurantCookRequest(@PathVariable Long restaurantId, @RequestBody OrderDTO orderDTO) {
        cookOrderService.submitNewCookOrderFromDTO(orderDTO);
        // we will not bring case that restaurant will not cook, placeholder response
        return "Accepted";
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
