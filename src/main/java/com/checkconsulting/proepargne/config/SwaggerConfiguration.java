package com.checkconsulting.proepargne.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Pro epargne API",
                contact = @Contact(
                        name = "M. Chekini",
                        url = "https://github.com/mchekini-check-consulting"
                ),
                version = "1"
        )
)
public class SwaggerConfiguration {
}
