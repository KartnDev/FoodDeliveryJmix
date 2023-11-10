package com.company.ordersystem.entity;

import com.company.useroidcplagin.entity.AppUser;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.datatype.impl.EnumUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.eclipse.persistence.indirection.IndirectList;
import org.springframework.util.Assert;

import java.util.List;

@JmixEntity
@Table(name = "OrderEntity", indexes = {
        @Index(name = "IDX_ORDER_BELONGS_TO_USER", columnList = "BELONGS_TO_USER_ID")
})
@Entity
public class OrderEntity {

    @Id
    @InstanceName
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @NotNull
    private Long id;

    @Column(name = "RESTAURANT_NAME")
    private String restaurantName;

    @Column(name = "RESTAURANT_ID")
    private Long restaurantId;

    @Column(name = "IS_CURRENT_ORDER_FOR_USER")
    private Boolean isCurrentOrderForUser = false;

    @JoinColumn(name = "BELONGS_TO_USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser belongsToUser;

    @OneToMany(mappedBy = "order")
    private List<FoodItemCountedEntity> orderItems = new IndirectList<>();

    @NotNull
    @Column(name = "STATUS", nullable = false)
    private String status = DraftOrderStatus.NEW_ORDER.getId();

    public DraftOrderStatus getStatus() {
        return EnumUtils.fromIdSafe(DraftOrderStatus.class, status, DraftOrderStatus.NEW_ORDER);
    }

    public void setStatus(DraftOrderStatus status) {
        Assert.notNull(status, "Status must be not null");
        this.status = status.getId();
    }

    public void setIsCurrentOrderForUser(Boolean isCurrentOrderForUser) {
        this.isCurrentOrderForUser = isCurrentOrderForUser;
    }

    public Boolean getIsCurrentOrderForUser() {
        return isCurrentOrderForUser;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public AppUser getBelongsToUser() {
        return belongsToUser;
    }

    public void setBelongsToUser(AppUser belongsToUser) {
        this.belongsToUser = belongsToUser;
    }

    public List<FoodItemCountedEntity> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<FoodItemCountedEntity> orderItems) {
        this.orderItems = orderItems;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}