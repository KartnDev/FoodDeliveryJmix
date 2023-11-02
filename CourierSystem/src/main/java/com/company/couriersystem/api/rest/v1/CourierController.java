package com.company.couriersystem.api.rest.v1;

import com.company.couriersystem.security.FullAccessRole;
import com.company.couriersystem.service.CourierService;
import com.company.orderapi.model.OrderDTO;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Secured(FullAccessRole.CODE)
@RestController
@RequestMapping(value = "api/v1")
public class CourierController {
    private final CourierService courierService;

    public CourierController(CourierService courierService) {
        this.courierService = courierService;
    }

    @PostMapping("/orders/{orderId}")
    public String getRestaurantCookRequest(@PathVariable Long orderId, @RequestBody OrderDTO orderDTO) {
        courierService.submitNewDeliveryOrderFromDTO(orderDTO);
        // we will not bring case that restaurant will not cook, placeholder response
        return "Accepted";
    }
}
