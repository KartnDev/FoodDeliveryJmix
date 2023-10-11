package com.company.useroidcplagin.impl;

import com.company.useroidcplagin.entity.AppUser;
import io.jmix.core.UnconstrainedDataManager;
import io.jmix.core.security.UserRepository;
import io.jmix.oidc.claimsmapper.ClaimsRolesMapper;
import io.jmix.oidc.usermapper.SynchronizingOidcUserMapper;
import io.jmix.security.authentication.RoleGrantedAuthority;
import io.jmix.security.role.ResourceRoleRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component("appuser_DeliverySynchronizingOidcUserMapper")
public class DeliverySynchronizingOidcUserMapper extends SynchronizingOidcUserMapper<AppUser> {

    public static final String REALM_ROLE_CLAIM = "realm-role";
    protected final ResourceRoleRepository resourceRoleRepository;

    public DeliverySynchronizingOidcUserMapper(UnconstrainedDataManager dataManager,
                                               UserRepository userRepository,
                                               ClaimsRolesMapper claimsRolesMapper,
                                               ResourceRoleRepository resourceRoleRepository) {
        super(dataManager, userRepository, claimsRolesMapper);
        this.resourceRoleRepository = resourceRoleRepository;

        //store role assignments in the database (false by default)
        setSynchronizeRoleAssignments(true);
    }

    @Override
    protected Class<AppUser> getApplicationUserClass() {
        return AppUser.class;
    }

    @Override
    protected void populateUserAttributes(OidcUser oidcUser, AppUser appUser) {
        appUser.setUsername(oidcUser.getName());
        appUser.setFirstName(oidcUser.getGivenName());
        appUser.setLastName(oidcUser.getFamilyName());
        appUser.setEmail(oidcUser.getEmail());
    }

    @Override
    protected void populateUserAuthorities(OidcUser oidcUser, AppUser jmixUser) {
        //noinspection unchecked
        jmixUser.setAuthorities(((ArrayList<String>) oidcUser.getClaim(REALM_ROLE_CLAIM))
                .stream()
                .filter(e -> resourceRoleRepository.findRoleByCode(e) != null)
                .map(e -> RoleGrantedAuthority.ofResourceRole(resourceRoleRepository.getRoleByCode(e)))
                .toList());
    }
}
