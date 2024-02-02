package com.checkconsulting.proepargne.service;

import com.checkconsulting.proepargne.dto.UserDTO;
import com.checkconsulting.proepargne.exceptions.KeycloakException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;

@Slf4j
@Service
public class KeycloakUserService {

    //    @Value("${keycloak.serverUrl}")
    final static String serverUrl = "https://keycloak.check-consulting.net/auth";
    //    @Value("${keycloak.master}")
    final static String realm = "masterxx";
    //    @Value("${keycloak.clientId}")
    final static String clientId = "collab-test";
    //    @Value("${keycloak.userName}")
    final static String userName = "admin";
    //    @Value("${keycloak.password}")
    final static String password = "admin";

    public void addUser(UserDTO userDTO) {
        try {
            Keycloak keycloak = Keycloak.getInstance(
                    serverUrl,
                    realm,
                    userName,
                    password,
                    clientId
            );

            RealmResource realmResource = keycloak.realm(realm);
            UserRepresentation user = new UserRepresentation();
            user.setUsername(userDTO.getUserName());
            user.setEmail(userDTO.getEmailId());
            user.setEnabled(true);

            realmResource.users().create(user);
        } catch (NotFoundException e) {
            throw new NotFoundException("Royaume/Client Keycloak n'existe pas");
        } catch (KeycloakException e) {
            throw new KeycloakException("Erreur lors de la création de l'employé sur Keycloak");
        }
    }

}