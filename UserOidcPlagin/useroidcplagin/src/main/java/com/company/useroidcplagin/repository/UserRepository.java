package com.company.useroidcplagin.repository;

import com.company.useroidcplagin.entity.AppUser;
import io.jmix.core.repository.JmixDataRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JmixDataRepository<AppUser, UUID> {
    AppUser getAppUserByUsername(String username);
    Optional<AppUser> findAppUserByUsername(String username);
}
