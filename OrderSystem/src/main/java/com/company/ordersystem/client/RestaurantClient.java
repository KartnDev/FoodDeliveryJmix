package com.company.ordersystem.client;

import com.company.restaurantapi.entity.RestaurantDTO;
import com.company.restaurantapi.entity.RestaurantMenuDTO;
import com.company.useroidcplagin.holder.OidcTokenHolder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.text.MessageFormat;
import java.util.List;


@Component
@Slf4j
public class RestaurantClient {

    private final WebClient webClient;
    private final OidcTokenHolder oidcTokenHolder;

    @Value("${systems.restaurant.url}")
    private String restaurantUrl;

    public RestaurantClient(WebClient webClient, OidcTokenHolder oidcTokenHolder) {
        this.webClient = webClient;
        this.oidcTokenHolder = oidcTokenHolder;
    }

    @SneakyThrows
    public List<RestaurantDTO> listRestaurants() {
        String url = MessageFormat.format("{0}/api/v1/restaurants", restaurantUrl);
        return webClient.get()
                .uri(new URI(url))
                .header("Authorization", MessageFormat.format("Bearer {0}", oidcTokenHolder.getCurrentUserToken()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(RestaurantDTO.class)
                .collectList()
                .block();
    }

    @SneakyThrows
    public List<RestaurantMenuDTO> listRestaurantMenus(Long restaurantId) {
        String url = MessageFormat.format("{0}/api/v1/restaurants/{1}/menus", restaurantUrl, restaurantId);
        return webClient.get()
                .uri(new URI(url))
                .header("Authorization", MessageFormat.format("Bearer {0}", oidcTokenHolder.getCurrentUserToken()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(RestaurantMenuDTO.class)
                .collectList()
                .block();
    }
}
