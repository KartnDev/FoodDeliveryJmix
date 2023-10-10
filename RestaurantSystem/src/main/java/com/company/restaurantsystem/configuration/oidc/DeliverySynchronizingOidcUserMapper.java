package com.company.restaurantsystem.configuration.oidc;

import com.company.restaurantsystem.entity.AppUser;
import io.jmix.core.UnconstrainedDataManager;
import io.jmix.core.security.UserRepository;
import io.jmix.oidc.claimsmapper.ClaimsRolesMapper;
import io.jmix.oidc.usermapper.SynchronizingOidcUserMapper;
import io.jmix.security.authentication.RoleGrantedAuthority;
import io.jmix.security.role.ResourceRoleRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class DeliverySynchronizingOidcUserMapper extends SynchronizingOidcUserMapper<AppUser> {

    public DeliverySynchronizingOidcUserMapper(UnconstrainedDataManager dataManager,
                                               UserRepository userRepository,
                                               ClaimsRolesMapper claimsRolesMapper,
                                               ResourceRoleRepository resourceRoleRepository) {
        super(dataManager, userRepository, claimsRolesMapper);
        this.resourceRoleRepository = resourceRoleRepository;
        //store role assignments in the database (false by default)
        setSynchronizeRoleAssignments(true);
    }

    protected final ResourceRoleRepository resourceRoleRepository;

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
        jmixUser.setAuthorities(List.of(RoleGrantedAuthority.ofResourceRole(resourceRoleRepository.getRoleByCode("system-full-access"))));
    }
}
