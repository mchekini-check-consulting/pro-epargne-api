package com.checkconsulting.proepargne.configuration;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfiguration {


    static Keycloak keycloak = null;
    final static String serverUrl = "https://keycloak.check-consulting.net/";
    public final static String realm = "master";
    final static String clientId = "pro-epargne";
    final static String clientSecret = "YOUR_CLIENT_SECRET_KEY";
    final static String userName = "admin";
    final static String password = "admin";

    public KeycloakConfiguration() {
    }

    public static Keycloak getInstance() {
        if (keycloak == null) {

            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(userName)
                    .password(password)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                    .build();
        }
        return keycloak;
    }
}
