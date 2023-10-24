package com.company.useroidcplagin.holder;


import com.vaadin.flow.spring.annotation.UIScope;
import io.jmix.core.security.CurrentAuthentication;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Secured("system-full-access")
@Component
@AllArgsConstructor
public class OidcTokenHolder {

    private final CurrentAuthentication currentAuthentication;

    private final Map<String, String> userNameTokenMapping = new ConcurrentHashMap<>();

    public String getCurrentUserToken() {
        return userNameTokenMapping.get(currentAuthentication.getUser().getUsername());
    }

    public void addCurrentUserToken(String username, String userToken) {
        userNameTokenMapping.put(username, userToken);
    }
}
