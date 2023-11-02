package com.company.ordersystem.api.rest.v1;

import com.company.ordersystem.security.FullAccessRole;
import com.company.ordersystem.service.process.order.OrderProcessManager;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Secured(FullAccessRole.CODE)
@RestController
@RequestMapping(value = "api/v1")
public class OrderProcessController {
    private final OrderProcessManager orderProcessManager;

    public OrderProcessController(OrderProcessManager orderProcessManager) {
        this.orderProcessManager = orderProcessManager;
    }

    @PostMapping("/orders/{orderId}/restaurantstep/{restaurantId}")
    public void continueOrderRestaurantStep(@PathVariable String orderId, @PathVariable String restaurantId) {
        orderProcessManager.continueProcessByRestaurantStep(orderId, restaurantId);
    }

    @PostMapping("/orders/{orderId}/courierFoundStep/")
    public void continueOrderCourierFoundStep(@PathVariable String orderId) {
        orderProcessManager.continueProcessByCourierFoundStep(orderId);
    }

    @PostMapping("/orders/{orderId}/deliveredStep/")
    public void continueOrderDeliveredStep(@PathVariable String orderId) {
        orderProcessManager.continueProcessByDeliveredStep(orderId);
    }
}
