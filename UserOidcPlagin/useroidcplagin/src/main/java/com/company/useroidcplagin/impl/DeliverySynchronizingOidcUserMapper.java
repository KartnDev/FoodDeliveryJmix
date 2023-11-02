package com.company.useroidcplagin.impl;

import com.company.useroidcplagin.entity.AppUser;
import com.company.useroidcplagin.repository.UserRepository;
import io.jmix.core.SaveContext;
import io.jmix.core.UnconstrainedDataManager;
import io.jmix.core.security.SystemAuthenticationToken;
import io.jmix.core.security.SystemAuthenticator;
import io.jmix.data.PersistenceHints;
import io.jmix.oidc.claimsmapper.ClaimsRolesMapper;
import io.jmix.oidc.usermapper.SynchronizingOidcUserMapper;
import io.jmix.security.authentication.RoleGrantedAuthority;
import io.jmix.security.role.ResourceRoleRepository;
import io.jmix.securitydata.entity.RoleAssignmentEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component("appuser_DeliverySynchronizingOidcUserMapper")
public class DeliverySynchronizingOidcUserMapper extends SynchronizingOidcUserMapper<AppUser> {

    public static final String REALM_ROLE_CLAIM = "realm-role";
    protected final ResourceRoleRepository resourceRoleRepository;
    protected final UserRepository userRepository;
    protected final SystemAuthenticator systemAuthenticator;

    public DeliverySynchronizingOidcUserMapper(UnconstrainedDataManager dataManager,
                                               io.jmix.core.security.UserRepository springUserRepository,
                                               ClaimsRolesMapper claimsRolesMapper,
                                               ResourceRoleRepository resourceRoleRepository,
                                               UserRepository userRepository,
                                               SystemAuthenticator systemAuthenticator) {
        super(dataManager, springUserRepository, claimsRolesMapper);
        this.resourceRoleRepository = resourceRoleRepository;
        this.userRepository = userRepository;
        this.systemAuthenticator = systemAuthenticator;

        //store role assignments in the database (false by default)
        setSynchronizeRoleAssignments(true);
    }

    @Override
    protected Class<AppUser> getApplicationUserClass() {
        return AppUser.class;
    }

    @Override
    protected void populateUserAttributes(OidcUser oidcUser, AppUser appUser) {
        systemAuthenticator.runWithSystem(() ->
                userRepository.findAppUserByUsername(oidcUser.getPreferredUsername())
                        .ifPresent(existingAppUser -> appUser.setId(existingAppUser.getId())));
        appUser.setUsername(oidcUser.getPreferredUsername());
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

    @Override
    protected void saveJmixUserAndRoleAssignments(OidcUser oidcUser, AppUser jmixUser) {
        SaveContext saveContext = new SaveContext();
        systemAuthenticator.begin();
        if (synchronizeRoleAssignments) {
            String username = getOidcUserUsername(oidcUser);
            //disable soft-deletion to completely remove role assignment records from the database
            saveContext.setHint(PersistenceHints.SOFT_DELETION, false);
            List<RoleAssignmentEntity> existingRoleAssignmentEntities = dataManager.load(RoleAssignmentEntity.class)
                    .query("select e from sec_RoleAssignmentEntity e where e.username = :username")
                    .parameter("username", username)
                    .list();
            saveContext.removing(existingRoleAssignmentEntities);

            Collection<RoleAssignmentEntity> newRoleAssignmentEntities = buildRoleAssignmentEntities(username, jmixUser.getAuthorities());
            saveContext.saving(newRoleAssignmentEntities);
        }

        Optional<AppUser> appUserByUsername = userRepository.findAppUserByUsername(oidcUser.getPreferredUsername());
        if(appUserByUsername.isPresent()) {
            AppUser appUser = appUserByUsername.get();
            appUser.setUsername(jmixUser.getPreferredUsername());

            appUser.setFirstName(jmixUser.getGivenName());
            appUser.setLastName(jmixUser.getFamilyName());
            appUser.setEmail(jmixUser.getEmail());
            saveContext.saving(appUser);
        } else {
            saveContext.saving(jmixUser);
        }
        //persist user details and roles if needed
        dataManager.save(saveContext);
        systemAuthenticator.end();
    }
}
