package com.company.ordersystem.entity;

import com.company.useroidcplagin.entity.AppUser;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.datatype.impl.EnumUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.persistence.indirection.IndirectList;
import org.springframework.util.Assert;

import java.util.List;

@JmixEntity
@Table(name = "ORDER", indexes = {
        @Index(name = "IDX_ORDER_BELONGS_TO_USER", columnList = "BELONGS_TO_USER_ID")
})
@Entity
@Getter
@Setter
public class Order {

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
}