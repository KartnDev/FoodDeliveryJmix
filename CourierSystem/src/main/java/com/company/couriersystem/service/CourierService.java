package com.company.couriersystem.service;

import com.company.couriersystem.client.OrderClient;
import com.company.couriersystem.entity.DeliveryStatus;
import com.company.couriersystem.entity.RequestCourierEntity;
import com.company.couriersystem.repository.RequestCourierEntityRepository;
import com.company.orderapi.model.OrderDTO;
import com.company.useroidcplagin.entity.AppUser;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public class CourierService {
    private final DataManager dataManager;
    private final RequestCourierEntityRepository requestCourierEntityRepository;
    private final CurrentAuthentication currentAuthentication;
    private final OrderClient orderClient;

    public CourierService(DataManager dataManager,
                          RequestCourierEntityRepository requestCourierEntityRepository,
                          CurrentAuthentication currentAuthentication,
                          OrderClient orderClient) {
        this.dataManager = dataManager;
        this.requestCourierEntityRepository = requestCourierEntityRepository;
        this.currentAuthentication = currentAuthentication;
        this.orderClient = orderClient;
    }

    public void submitNewDeliveryOrderFromDTO(OrderDTO orderDTO) {
        RequestCourierEntity requestCourierEntity = dataManager.create(RequestCourierEntity.class);
        requestCourierEntity.setRestaurantId(orderDTO.getRestaurantId());
        requestCourierEntity.setRestaurantName(orderDTO.getRestaurantName());
        requestCourierEntity.setAssingeeCourier(null);
        requestCourierEntity.setItemsTicketHtml(generateItemsTicket(orderDTO));
        requestCourierEntity.setStatus(DeliveryStatus.NEW_REQUEST);
        requestCourierEntity.setOrderId(orderDTO.getOriginOrderId());
        dataManager.save(requestCourierEntity);
    }

    private String generateItemsTicket(OrderDTO orderDTO) {
        StringBuilder stringBuilder = new StringBuilder();
        orderDTO.getItems().forEach(item -> stringBuilder.append(
                MessageFormat.format("<div></br> {0}, <strong>x{1}</strong></div>",
                        item.getFoodItemName(), item.getCount())));

        return "<div>" +
                "<h3> " + orderDTO.getRestaurantName() + " </h3>" +
                stringBuilder +
                "</div>";
    }

    public Optional<RequestCourierEntity> findDeliveryForCurrentUser() {
        AppUser currentUser = (AppUser) currentAuthentication.getUser();
        List<RequestCourierEntity> listRequestEntities =
                requestCourierEntityRepository.findRequestCourierEntitiesByAssingeeCourierAndStatus(
                        currentUser, DeliveryStatus.NEW_REQUEST.getId());
        if (listRequestEntities.isEmpty()) {
            return Optional.empty();
        } else if (listRequestEntities.size() > 1) {
            throw new IllegalStateException("For current user request size greater then 2, that is illegal");
        }
        return Optional.of(listRequestEntities.get(0));
    }

    public void publishAssignRequestForCurrentUser(RequestCourierEntity requestCourierEntity) {
        requestCourierEntity.setAssingeeCourier((AppUser) currentAuthentication.getUser());
        requestCourierEntity.setStatus(DeliveryStatus.ON_THE_WAY);
        orderClient.publishOrderCourierFoundStep(requestCourierEntity.getOrderId());
    }

    public void publishRequestDeliveredForCurrentUser(RequestCourierEntity requestCourierEntity) {
        requestCourierEntity.setStatus(DeliveryStatus.DONE);
        orderClient.publishOrderDelivered(requestCourierEntity.getOrderId());
    }
}
