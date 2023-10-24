package com.company.useroidcplagin;

import com.vaadin.flow.spring.annotation.UIScope;
import io.jmix.core.annotation.JmixModule;
import io.jmix.core.impl.scanning.AnnotationScanMetadataReaderFactory;
import io.jmix.core.repository.EnableJmixDataRepositories;
import io.jmix.eclipselink.EclipselinkConfiguration;
import io.jmix.flowui.FlowuiConfiguration;
import io.jmix.flowui.sys.ActionsConfiguration;
import io.jmix.flowui.sys.ViewControllersConfiguration;
import jakarta.servlet.http.HttpServletRequest;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;

@Configuration
@ComponentScan
@ConfigurationPropertiesScan
@JmixModule(dependsOn = {EclipselinkConfiguration.class, FlowuiConfiguration.class})
@PropertySource(name = "com.company.useroidcplagin", value = "classpath:/com/company/useroidcplagin/module.properties")
@EnableJmixDataRepositories
public class AppuserConfiguration {

}
