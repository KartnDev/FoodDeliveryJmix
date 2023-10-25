package com.company.restaurantsystem.api.rest.v1;

import com.company.restaurantapi.model.RestaurantDTO;
import com.company.restaurantsystem.repository.RestaurantRepository;
import com.company.restaurantsystem.service.AttachmentService;
import io.jmix.core.DataManager;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.company.restaurantsystem.security.FullAccessRole;

import java.util.List;
import java.util.stream.StreamSupport;

@Secured(FullAccessRole.CODE)
@RestController
@AllArgsConstructor
@RequestMapping(value = "api/v1")
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;
    private final DataManager dataManager;
    private final AttachmentService attachmentService;

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

}
