package com.company.ordersystem.client;

import com.company.orderapi.model.OrderDTO;
import com.company.restaurantapi.model.RestaurantDTO;
import com.company.restaurantapi.model.RestaurantMenuDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;


@Component
public class RestaurantClient extends AbstractClient {

    @Value("${systems.restaurant.url}")
    private String restaurantUrl;

    public List<RestaurantDTO> listRestaurants(String subjectToken) {
        String url = MessageFormat.format("{0}/api/v1/restaurants", restaurantUrl);
        return getApi(url, HttpMethod.GET, new ParameterizedTypeReference<List<RestaurantDTO>>() {}, null, subjectToken);
    }

    public RestaurantDTO getRestaurantById(Long restaurantId, String subjectToken) {
        String url = MessageFormat.format("{0}/api/v1/restaurants/{1,number,#}", restaurantUrl, restaurantId);
        return getApi(url, HttpMethod.GET, new ParameterizedTypeReference<RestaurantDTO>() {}, null, subjectToken);
    }

    public List<RestaurantMenuDTO> listRestaurantMenus(Long restaurantId, String subjectToken) {
        String url = MessageFormat.format("{0}/api/v1/restaurants/{1,number,#}/menus", restaurantUrl, restaurantId);
        return getApi(url, HttpMethod.GET, new ParameterizedTypeReference<List<RestaurantMenuDTO>>() {}, null, subjectToken);
    }

    public String publishRestaurantCookRequest(Long restaurantId, OrderDTO orderDTO, String subjectToken) {
        String url = MessageFormat.format("{0}/api/v1/restaurants/{1,number,#}/cook", restaurantUrl, restaurantId);
        return getApi(url, HttpMethod.POST, new ParameterizedTypeReference<String>() {}, orderDTO, subjectToken);
    }


}
