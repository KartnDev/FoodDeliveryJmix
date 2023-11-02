package com.company.couriersystem.security;

import com.company.useroidcplagin.entity.AppUser;
import io.jmix.securitydata.user.AbstractDatabaseUserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Primary
@Component("UserRepository")
public class DatabaseUserRepository extends AbstractDatabaseUserRepository<AppUser> {

    @Override
    protected Class<AppUser> getUserClass() {
        return AppUser.class;
    }

    @Override
    protected void initSystemUser(final AppUser systemUser) {
        final Collection<GrantedAuthority> authorities = getGrantedAuthoritiesBuilder()
                .addResourceRole(FullAccessRole.CODE)
                .build();
        systemUser.setAuthorities(authorities);
    }

    @Override
    protected void initAnonymousUser(final AppUser anonymousUser) {
    }
}