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
import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
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
    public String getAttachmentName() {
        return MessageFormat.format("restaurant_{0}_icon.png", name);
    }

    @Override
    public void setAttachment(FileRef attachment) {
        this.icon = attachment;
    }
}