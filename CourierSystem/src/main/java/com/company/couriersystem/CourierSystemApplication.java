package com.company.couriersystem;

import com.google.common.base.Strings;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import io.jmix.core.repository.EnableJmixDataRepositories;
import io.jmix.core.security.AuthorizedUrlsProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Push
@Theme(value = "CourierSystem")
@PWA(name = "CourierSystem", shortName = "CourierSystem")
@SpringBootApplication
@EnableJmixDataRepositories
@EnableWebMvc
public class CourierSystemApplication implements AppShellConfigurator {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(CourierSystemApplication.class, args);
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource")
    DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource.hikari")
    DataSource dataSource(final DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @EventListener
    public void printApplicationUrl(final ApplicationStartedEvent event) {
        LoggerFactory.getLogger(CourierSystemApplication.class).info("Application started at "
                + "http://localhost:"
                + environment.getProperty("local.server.port")
                + Strings.nullToEmpty(environment.getProperty("server.servlet.context-path")));
    }

    @Bean
    public RestTemplate defaultRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public AuthorizedUrlsProvider myAuthorizedUrlsProvider() {
        return new AuthorizedUrlsProvider() {
            @Override
            public Collection<String> getAuthenticatedUrlPatterns() {
                return List.of("/api/**");
            }

            @Override
            public Collection<String> getAnonymousUrlPatterns() {
                return new ArrayList<>();
            }
        };
    }
}
