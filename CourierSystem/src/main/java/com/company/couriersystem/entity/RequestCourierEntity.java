package com.company.couriersystem.entity;

import com.company.useroidcplagin.entity.AppUser;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.datatype.impl.EnumUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@JmixEntity
@Table(name = "REQUEST_COURIER_ENTITY", indexes = {
        @Index(name = "IDX_REQUEST_COURIER_ENTITY_ASSINGEE_COURIER", columnList = "ASSINGEE_COURIER_ID")
})
@Entity
public class RequestCourierEntity {
    @InstanceName
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "ORDER_ID")
    private Long orderId;

    @Column(name = "STATUS")
    private String status = DeliveryStatus.NEW_REQUEST.getId();

    @Column(name = "RESTAURANT_NAME")
    private String restaurantName;

    @Column(name = "RESTAURANT_ID")
    private Long restaurantId;

    @Column(name = "ITEMS_TICKET")
    private String itemsTicketHtml;

    @JoinColumn(name = "ASSINGEE_COURIER_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private AppUser assingeeCourier;

    public DeliveryStatus getStatus() {
        return EnumUtils.fromId(DeliveryStatus.class, status);
    }

    public void setStatus(@NotNull DeliveryStatus status) {
        this.status = status.getId();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getItemsTicketHtml() {
        return itemsTicketHtml;
    }

    public void setItemsTicketHtml(String itemsTicketHtml) {
        this.itemsTicketHtml = itemsTicketHtml;
    }

    public AppUser getAssingeeCourier() {
        return assingeeCourier;
    }

    public void setAssingeeCourier(AppUser assingeeCourier) {
        this.assingeeCourier = assingeeCourier;
    }
}