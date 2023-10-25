package com.company.ordersystem.entity;

import com.company.useroidcplagin.entity.AppUser;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "DRAFT_ORDER", indexes = {
        @Index(name = "IDX_DRAFT_ORDER_BELONGS_TO_USER", columnList = "BELONGS_TO_USER_ID")
})
@Entity
@Getter
@Setter
public class DraftOrder {

    @InstanceName
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @JoinColumn(name = "BELONGS_TO_USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser belongsToUser;

    @OneToMany(mappedBy = "draftOrder")
    private List<FoodItemCountedEntity> draftItems;
}