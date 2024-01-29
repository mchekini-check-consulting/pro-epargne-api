package com.checkconsulting.proepargne.resources;


import com.checkconsulting.proepargne.configuration.Credentials;
import com.checkconsulting.proepargne.configuration.KeycloakConfiguration;
import com.checkconsulting.proepargne.model.AppInformation;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("api/v1/app")
@Slf4j
public class ApplicationInformationResource {


    private final AppInformation appInformation;

    public ApplicationInformationResource(AppInformation appInformation) {

        this.appInformation = appInformation;
    }

    @GetMapping
    public AppInformation getApplicationInformations() {
        return AppInformation.builder()
                .name(appInformation.getName())
                .version(appInformation.getVersion())
                .build();
    }


    @GetMapping("user")
    public String addUser() {
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials("admin");
        UserRepresentation user = new UserRepresentation();
        user.setUsername("mchekini");
        user.setFirstName("Mahdi");
        user.setLastName("CHEKINI");
        user.setEmail("me.chekini@gmail.com");
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        UsersResource instance = KeycloakConfiguration.getInstance().realm("master").users();
        instance.create(user);
        instance.list().stream()
                .filter(u -> u.getUsername().equals("mchekini"))
                .forEach(u -> {
                    instance.get(u.getId()).sendVerifyEmail();
                    CredentialRepresentation cred = new CredentialRepresentation();
                    cred.setType(CredentialRepresentation.PASSWORD);
                    cred.setTemporary(true);
                    cred.setValue("test");
                    instance.get(u.getId()).resetPassword(cred);
                });


        return "creation de user effectuée avec succès";
    }
}
