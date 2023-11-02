package com.company.ordersystem.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;

public class AbstractClient {

    private final Logger log = LoggerFactory.getLogger(AbstractClient.class);

    public <T, V> T getApi(String path,
                           HttpMethod method,
                           ParameterizedTypeReference<T> parameterizedTypeReference,
                           V body,
                           String subjectToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", MessageFormat.format("Bearer {0}", subjectToken));
        RequestEntity<V> requestEntity;
        try {
            requestEntity = new RequestEntity<>(body, headers, HttpMethod.GET, new URI(path));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        ResponseEntity<T> response = restTemplate.exchange(
                path,
                method,
                requestEntity,
                parameterizedTypeReference);
        if (response.getStatusCode().isError()) {
            log.error("Error code found when requesting " + path + " with code " + response.getStatusCode().value());
            throw new RuntimeException();
        }
        return response.getBody();
    }
}
