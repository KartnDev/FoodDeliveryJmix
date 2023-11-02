package com.company.couriersystem.repository;

import com.company.couriersystem.entity.DeliveryStatus;
import com.company.couriersystem.entity.RequestCourierEntity;
import com.company.useroidcplagin.entity.AppUser;
import io.jmix.core.repository.JmixDataRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RequestCourierEntityRepository extends JmixDataRepository<RequestCourierEntity, UUID> {
    List<RequestCourierEntity> findRequestCourierEntitiesByAssingeeCourierAndStatus(AppUser assingeeCourier, String status);
}
