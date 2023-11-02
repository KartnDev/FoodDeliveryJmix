package com.company.restaurantsystem.entity;

import com.company.restaurantsystem.model.HasIcon;
import io.jmix.core.DeletePolicy;
import io.jmix.core.FileRef;
import io.jmix.core.annotation.DeletedBy;
import io.jmix.core.annotation.DeletedDate;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDelete;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.JmixProperty;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "RESTAURANT_MENU_ITEM", indexes = {
        @Index(name = "IDX_RESTAURANT_MENU_ITEM_RESTAURANT_MENU", columnList = "RESTAURANT_MENU_ID"),
        @Index(name = "IDX_RESTAURANT_MENU_ITEM_RESTAURANT", columnList = "RESTAURANT_ID")
})
@Entity
public class RestaurantFoodItem implements HasIcon {

    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @InstanceName
    @Column(name = "NAME")
    private String name;

    @JmixProperty(mandatory = true)
    @JoinColumn(name = "RESTAURANT_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Restaurant restaurant;

    @ManyToMany
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @JoinTable(name = "RESTAURANT_MENU_RESTAURANT_FOOD_ITEM_LINK",
            joinColumns = @JoinColumn(name = "RESTAURANT_FOOD_ITEM_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "RESTAURANT_MENU_ID", referencedColumnName = "ID"))
    private List<RestaurantMenu> restaurantMenus;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ICON", length = 1024)
    private FileRef icon;

    @JmixProperty(mandatory = true)
    @Column(name = "VERSION", nullable = false)
    @Version
    private Integer version;

    @CreatedBy
    @Column(name = "CREATED_BY")
    private String createdBy;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    private OffsetDateTime createdDate;

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    private OffsetDateTime lastModifiedDate;

    @DeletedBy
    @Column(name = "DELETED_BY")
    private String deletedBy;

    @DeletedDate
    @Column(name = "DELETED_DATE")
    private OffsetDateTime deletedDate;

    @Column(name = "PRICE")
    private Integer price;

    @Override
    public FileRef getAttachment() {
        return icon;
    }

    @Override
    public void setAttachment(FileRef attachment) {
        this.icon = attachment;
    }

    @DependsOnProperties("restaurant")
    @Override
    public String getAttachmentName() {
        return MessageFormat.format("restaurant_{0}_item_{1}_icon.png", restaurant.getName(), name);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<RestaurantMenu> getRestaurantMenus() {
        return restaurantMenus;
    }

    public void setRestaurantMenus(List<RestaurantMenu> restaurantMenus) {
        this.restaurantMenus = restaurantMenus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FileRef getIcon() {
        return icon;
    }

    public void setIcon(FileRef icon) {
        this.icon = icon;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public OffsetDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(OffsetDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public OffsetDateTime getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(OffsetDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}