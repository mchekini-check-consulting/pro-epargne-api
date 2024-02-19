package com.checkconsulting.proepargne.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${keycloak.serverUrl}")
    String serverUrl;
    @Value("${keycloak.realm}")
    String realm;
    @Value("${keycloak.clientId}")
    String clientId;
    @Value("${keycloak.userName}")
    String userName;
    @Value("${keycloak.password}")
    String password;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .username(userName)
                .password(password)
                .clientId(clientId)
                .build();
    }
}

