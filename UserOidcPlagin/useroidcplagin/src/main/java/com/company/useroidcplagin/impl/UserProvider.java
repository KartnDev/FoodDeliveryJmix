package com.company.useroidcplagin.impl;

import com.company.useroidcplagin.entity.AppUser;
import com.company.useroidcplagin.repository.UserRepository;
import io.jmix.core.security.CurrentAuthentication;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserProvider {
    private final CurrentAuthentication currentAuthentication;
    private final UserRepository userRepository;

    @NotNull
    public AppUser getCurrentUser() {
        return userRepository.getAppUserByUsername(currentAuthentication.getUser().getUsername());
    }

    public Optional<AppUser> findCurrentUser() {
        return userRepository.findAppUserByUsername(currentAuthentication.getUser().getUsername());
    }
}
