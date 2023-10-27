package com.company.ordersystem.entity;

import com.company.restaurantapi.model.HasIcon;
import com.company.useroidcplagin.entity.AppUser;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@JmixEntity
@Table(name = "FOOD_ITEM_COUNTED_ENTITY", indexes = {
        @Index(name = "IDX_FOOD_ITEM_COUNTED_ENTITY_ITEM", columnList = "ITEM_ID"),
        @Index(name = "IDX_FOOD_ITEM_COUNTED_ENTITY_DRAFT_ORDER", columnList = "DRAFT_ORDER_ID")
})
@Entity
@Getter
@Setter
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

    @JoinColumn(name = "DRAFT_ORDER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

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
}