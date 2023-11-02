package com.company.restaurantsystem.client;

import com.company.orderapi.model.OrderDTO;
import com.company.useroidcplagin.entity.AppUser;
import io.jmix.core.security.CurrentAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;

@Component
public class OrderClient {

    private final Logger log = LoggerFactory.getLogger(OrderClient.class);

    private final CurrentAuthentication currentAuthentication;

    @Value("${systems.order.url}")
    private String restaurantUrl;

    public OrderClient(CurrentAuthentication currentAuthentication) {
        this.currentAuthentication = currentAuthentication;
    }

    public void publishRestaurantCookDone(Long restaurantId, Long orderId) {
        String url = MessageFormat.format("{0}/api/v1/orders/{1,number,#}/restaurantstep/{2,number,#}", restaurantUrl, orderId, restaurantId);
        final RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var user = (AppUser) currentAuthentication.getUser();

        headers.set("Authorization", MessageFormat.format("Bearer {0}", user.getUserToken()));

        final RequestEntity<OrderDTO> requestEntity;
        try {
            requestEntity = new RequestEntity<>(null, headers, HttpMethod.POST, new URI(url));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        final ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class);
        if(response.getStatusCode().isError()) {
            log.error("Error code found when requesting " + url + " with code " + response.getStatusCode().value());
            throw new RuntimeException();
        }
    }
}
