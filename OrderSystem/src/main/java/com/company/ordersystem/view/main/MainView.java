package com.company.ordersystem.view.main;

import com.company.ordersystem.client.RestaurantClient;
import com.company.restaurantapi.entity.RestaurantDTO;
import com.company.restaurantapi.entity.RestaurantMenuDTO;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.app.main.StandardMainView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Route("")
@ViewController("MainView")
@ViewDescriptor("main-view.xml")
public class MainView extends StandardMainView {
    @Autowired
    RestaurantClient restaurantClient;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        List<RestaurantDTO> restaurantDTOList = restaurantClient.listRestaurants();

        Map<RestaurantDTO, List<RestaurantMenuDTO>> mapping = restaurantDTOList.stream()
                .collect(Collectors.toMap(Function.identity(),
                        restaurantDTO -> restaurantClient.listRestaurantMenus(restaurantDTO.getId())));
    }


}
