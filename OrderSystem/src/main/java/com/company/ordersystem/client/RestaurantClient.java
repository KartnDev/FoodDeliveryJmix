package com.company.ordersystem.client;

import com.company.restaurantapi.model.RestaurantDTO;
import com.company.restaurantapi.model.RestaurantMenuDTO;
import com.company.useroidcplagin.holder.OidcTokenHolder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.common.util.MultivaluedHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.*;


@Component
@Slf4j
public class RestaurantClient {

    //when Spring 6.1 would be available use Blocking RestClient from Spring Web instead of nonblocking WebClient from reactive
    private final OidcTokenHolder oidcTokenHolder;

    @Value("${systems.restaurant.url}")
    private String restaurantUrl;

    public RestaurantClient(OidcTokenHolder oidcTokenHolder) {
        this.oidcTokenHolder = oidcTokenHolder;
    }

    public List<RestaurantDTO> listRestaurants() {
        String url = MessageFormat.format("{0}/api/v1/restaurants", restaurantUrl);
        return getApi(url, HttpMethod.GET, new ParameterizedTypeReference<List<RestaurantDTO>>() {});
    }

    public List<RestaurantMenuDTO> listRestaurantMenus(Long restaurantId) {
        String url = MessageFormat.format("{0}/api/v1/restaurants/{1}/menus", restaurantUrl, restaurantId);
        return getApi(url, HttpMethod.GET, new ParameterizedTypeReference<List<RestaurantMenuDTO>>() {});
    }

    public <T> T getApi(final String path, final HttpMethod method, final ParameterizedTypeReference<T> parameterizedTypeReference) {
        final RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", MessageFormat.format("Bearer {0}", oidcTokenHolder.getCurrentUserToken()));
        final RequestEntity<T> requestEntity;
        try {
            requestEntity = new RequestEntity<>(null, headers, HttpMethod.GET, new URI(path));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        final ResponseEntity<T> response = restTemplate.exchange(
                path,
                method,
                requestEntity,
                parameterizedTypeReference);
        if(response.getStatusCode().isError()) {
            log.error("Error code found when requesting " + path + " with code " + response.getStatusCode().value());
            throw new RuntimeException();
        }
        return response.getBody();
    }
}
