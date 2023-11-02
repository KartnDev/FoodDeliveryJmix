package com.company.ordersystem.entity;

import com.company.restaurantapi.model.HasIcon;
import com.company.useroidcplagin.entity.AppUser;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.util.UUID;

@JmixEntity
@Table(name = "FOOD_ITEM_COUNTED_ENTITY", indexes = {
        @Index(name = "IDX_FOOD_ITEM_COUNTED_ENTITY_ITEM", columnList = "ITEM_ID"),
        @Index(name = "IDX_FOOD_ITEM_COUNTED_ENTITY_ORDER", columnList = "ORDER_ID")
})
@Entity
public class FoodItemCountedEntity implements HasIcon {

    @InstanceName
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @JoinColumn(name = "ITEM_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private RestaurantFoodItemReplica item;

    @Column(name = "COUNT_")
    private Integer count;

    @JoinColumn(name = "BELONGS_TO_USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser belongsToUser;

    @JoinColumn(name = "ORDER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderEntity order;

    @Override
    public byte[] getIcon() {
        return item.getIcon();
    }

    @Override
    public void setIcon(byte[] icon) {
        item.setIcon(icon);
    }

    @Override
    public String getAttachmentName() {
        return item.getAttachmentName();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RestaurantFoodItemReplica getItem() {
        return item;
    }

    public void setItem(RestaurantFoodItemReplica item) {
        this.item = item;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public AppUser getBelongsToUser() {
        return belongsToUser;
    }

    public void setBelongsToUser(AppUser belongsToUser) {
        this.belongsToUser = belongsToUser;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }
}