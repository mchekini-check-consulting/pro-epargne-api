package com.checkconsulting.proepargne.resources;

import com.checkconsulting.proepargne.aspect.authentication.Authenticated;
import com.checkconsulting.proepargne.model.AppInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/app")
@Slf4j
public class ApplicationInformationResource {

    private final AppInformation appInformation;

    public ApplicationInformationResource(AppInformation appInformation) {

        this.appInformation = appInformation;
    }

    @GetMapping
    @Authenticated(authenticated = true)
    public AppInformation getApplicationInformations() {

        log.info("un appel a ete effectue");

        return AppInformation.builder()
                .name(appInformation.getName())
                .version(appInformation.getVersion())
                .build();
    }

}
