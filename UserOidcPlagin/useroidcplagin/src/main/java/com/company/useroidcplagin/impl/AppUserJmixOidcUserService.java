package com.company.useroidcplagin.impl;

import com.company.useroidcplagin.holder.OidcTokenHolder;
import io.jmix.oidc.user.JmixOidcUser;
import io.jmix.oidc.userinfo.DefaultJmixOidcUserService;
import io.jmix.oidc.usermapper.OidcUserMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

@Primary
@Component
public class AppUserJmixOidcUserService extends DefaultJmixOidcUserService {

    private final OidcTokenHolder oidcTokenHolder;

    public AppUserJmixOidcUserService(OidcUserMapper<? extends JmixOidcUser> userMapper, OidcTokenHolder oidcTokenHolder) {
        super(userMapper);
        this.oidcTokenHolder = oidcTokenHolder;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        oidcTokenHolder.addCurrentUserToken(userRequest.getIdToken().getPreferredUsername(), userRequest.getAccessToken().getTokenValue());
        return super.loadUser(userRequest);
    }


}
