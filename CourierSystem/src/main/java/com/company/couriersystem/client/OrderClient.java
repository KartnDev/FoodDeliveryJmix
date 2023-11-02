package com.company.couriersystem.client;

import com.company.useroidcplagin.entity.AppUser;
import io.jmix.core.security.CurrentAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
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
    private String orderUrl;

    public OrderClient(CurrentAuthentication currentAuthentication) {
        this.currentAuthentication = currentAuthentication;
    }

    public void publishOrderCourierFoundStep(Long orderId) {
        String url = MessageFormat.format("{0}/api/v1/orders/{1,number,#}/courierFoundStep/", orderUrl, orderId);
        getApi(url, HttpMethod.POST, new ParameterizedTypeReference<String>() {});
    }

    public void publishOrderDelivered(Long orderId) {
        String url = MessageFormat.format("{0}/api/v1/orders/{1,number,#}/deliveredStep/", orderUrl, orderId);
        getApi(url, HttpMethod.POST, new ParameterizedTypeReference<String>() {});
    }

    public <T, V> void getApi(String path, HttpMethod method, ParameterizedTypeReference<T> parameterizedTypeReference) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var user = (AppUser) currentAuthentication.getUser();

        headers.set("Authorization", MessageFormat.format("Bearer {0}", user.getUserToken()));
        RequestEntity<V> requestEntity;
        try {
            requestEntity = new RequestEntity<>(null, headers, method, new URI(path));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        ResponseEntity<T> response = restTemplate.exchange(
                path,
                method,
                requestEntity,
                parameterizedTypeReference);
        if(response.getStatusCode().isError()) {
            log.error("Error code found when requesting " + path + " with code " + response.getStatusCode().value());
            throw new RuntimeException();
        }
    }
}
