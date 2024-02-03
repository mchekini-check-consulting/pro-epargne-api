package com.checkconsulting.proepargne.service;

import com.checkconsulting.proepargne.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

@Slf4j
@Service
public class KeycloakUserService {

    @Value("${keycloak.realm}")
    String realm;

    final Keycloak keycloak;

    public KeycloakUserService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public String addUser(UserDTO userDTO) {
        RealmResource realmResource = keycloak.realm(realm);

        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUserName());
        user.setEmail(userDTO.getEmail());
        user.setLastName(userDTO.getLastName());
        user.setFirstName(userDTO.getFirstname());
        user.setEnabled(true);

        Response response = realmResource.users().create(user);

        realmResource.users().list().stream()
                .filter(u -> String.valueOf(u.getEmail()).equals(userDTO.getEmail()))
                .forEach(u -> {
                    realmResource.users().get(u.getId()).sendVerifyEmail();
                    CredentialRepresentation cred = new CredentialRepresentation();
                    cred.setType(CredentialRepresentation.PASSWORD);
                    cred.setTemporary(true);
                    cred.setValue("test");
                    realmResource.users().get(u.getId()).resetPassword(cred);
                });

        String userUri = response.getLocation().toString();

        return userUri.substring(userUri.lastIndexOf('/') + 1);

    }

    public void updateUser(String userId, UserDTO userDTO) {

        try {
            RealmResource realmResource = keycloak.realm(realm);

            UserRepresentation user = new UserRepresentation();
            user.setUsername(userDTO.getUserName());
            user.setFirstName(userDTO.getFirstname());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());

            realmResource.users().get(userId).update(user);
            log.info("User updated in keycloak");

        } catch (NotFoundException e) {
            throw new NotFoundException("Royaume/Client non trouvé sur Keycloak");
        } catch (ForbiddenException e) {
            throw new ForbiddenException("Vous n'avez pas le droit de modification d'utilisateur sur ce royaume");
        }
    }

}