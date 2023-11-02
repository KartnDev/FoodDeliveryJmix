package com.company.restaurantsystem.entity;

import com.company.restaurantsystem.model.HasIcon;
import com.company.useroidcplagin.entity.AppUser;
import io.jmix.core.DeletePolicy;
import io.jmix.core.FileRef;
import io.jmix.core.annotation.DeletedBy;
import io.jmix.core.annotation.DeletedDate;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDelete;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
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
@Table(name = "RESTAURANT")
@Entity
public class Restaurant implements HasIcon {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private Long id;

    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantFoodItem> foodItems;

    @OnDelete(DeletePolicy.UNLINK)
    @JoinTable(name = "RESTAURANT_APP_USER_LINK",
            joinColumns = @JoinColumn(name = "RESTAURANT_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "APP_USER_ID", referencedColumnName = "ID"))
    @ManyToMany
    private List<AppUser> owners;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantMenu> menus;

    @InstanceName
    @Column(name = "NAME")
    private String name;

    @Column(name = "ICON", length = 1024)
    private FileRef icon;

    @Column(name = "DESCRIPTION")
    private String description;

    @JmixGeneratedValue
    @Column(name = "UUID")
    private UUID uuid;

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

    @Override
    public FileRef getAttachment() {
        return icon;
    }

    @Override
    public void setAttachment(FileRef attachment) {
        this.icon = attachment;
    }

    @Override
    public String getAttachmentName() {
        return MessageFormat.format("restaurant_{0}_icon.png", name);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<RestaurantFoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<RestaurantFoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    public List<AppUser> getOwners() {
        return owners;
    }

    public void setOwners(List<AppUser> owners) {
        this.owners = owners;
    }

    public List<RestaurantMenu> getMenus() {
        return menus;
    }

    public void setMenus(List<RestaurantMenu> menus) {
        this.menus = menus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileRef getIcon() {
        return icon;
    }

    public void setIcon(FileRef icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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
}