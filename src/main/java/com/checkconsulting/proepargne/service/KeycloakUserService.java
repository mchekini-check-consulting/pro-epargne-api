package com.checkconsulting.proepargne.service;

import com.checkconsulting.proepargne.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

@Slf4j
@Service
public class KeycloakUserService {

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


    final Keycloak keycloak;

    public KeycloakUserService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public String addUser(UserDTO userDTO) {
        try {
            RealmResource realmResource = keycloak.realm(realm);

            UserRepresentation user = new UserRepresentation();
            user.setUsername(userDTO.getUserName());
            user.setEmail(userDTO.getEmailId());
            user.setLastName(userDTO.getLastName());
            user.setFirstName(userDTO.getFirstname());
            user.setEnabled(true);

            Response response = realmResource.users().create(user);

            String userUri = response.getLocation().toString();

            String userId = userUri.substring(userUri.lastIndexOf('/') + 1);

            return userId;

        } catch (NotFoundException e) {
            throw new NotFoundException("Royaume/Client non trouvé sur Keycloak");
        } catch (ForbiddenException e) {
            throw new ForbiddenException("Vous n'avez pas le droit de création d'utilisateur sur ce royaume");
        }
    }

    public void updateUser(String userId, UserDTO userDTO) {

        try {
            RealmResource realmResource = keycloak.realm(realm);

            UserRepresentation user = new UserRepresentation();
            user.setUsername(userDTO.getUserName());
            user.setFirstName(userDTO.getFirstname());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmailId());

            realmResource.users().get(userId).update(user);

        } catch (NotFoundException e) {
            throw new NotFoundException("Royaume/Client non trouvé sur Keycloak");
        } catch (ForbiddenException e) {
            throw new ForbiddenException("Vous n'avez pas le droit de modification d'utilisateur sur ce royaume");
        }
    }

}