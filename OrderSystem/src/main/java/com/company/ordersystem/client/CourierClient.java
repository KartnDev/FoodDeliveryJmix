package com.company.ordersystem.client;

import com.company.orderapi.model.OrderDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class CourierClient extends AbstractClient {

    @Value("${systems.courier.url}")
    private String courierSystemUrl;

    public String requestForCourier(OrderDTO orderDTO, String subjectToken) {
        String url = MessageFormat.format("{0}/api/v1/orders/{1,number,#}", courierSystemUrl, orderDTO.getOriginOrderId());
        return getApi(url, HttpMethod.POST, new ParameterizedTypeReference<String>() {}, orderDTO, subjectToken);
    }
}
