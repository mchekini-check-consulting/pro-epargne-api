package com.checkconsulting.proepargne.security;

import com.checkconsulting.proepargne.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@Slf4j
public class SecurityFilter implements Filter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String SPACE_SEPARATOR = " ";
    private final User user;


    @Value("${oauth2.resource-server.jwt.issuer-uri-collab}")
    String collabIssuerUri;

    @Value("${oauth2.resource-server.jwt.issuer-uri-admin}")
    String adminIssuerUri;

    public SecurityFilter(User user) {
        this.user = user;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader != null && authorizationHeader.split(SPACE_SEPARATOR).length == 2) {
            String token = request.getHeader(AUTHORIZATION_HEADER).split(SPACE_SEPARATOR)[1];

            String tokenClaims = JwtHelper.decode(token).getClaims();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> claimMap = objectMapper.readValue(tokenClaims, new TypeReference<>() {
            });

            if (claimMap.get("iss").toString().endsWith("pro-epargne-collab")) {
                decodeTokenAndPopulateUser(token, collabIssuerUri);

            } else if (claimMap.get("iss").toString().endsWith("pro-epargne-admin")) {
                decodeTokenAndPopulateUser(token, adminIssuerUri);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

    private void decodeTokenAndPopulateUser(String token, String issuer) {
        JwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuer);
        try {
            Jwt jwt = jwtDecoder.decode(token);

            user.setUserName(jwt.getClaims().get("preferred_username").toString());
            user.setEmail(jwt.getClaims().get("email").toString());
            user.setAuthenticated(true);
            user.setKeycloakId(jwt.getClaims().get("sub").toString());
            List<String> roles = (List<String>) ((LinkedTreeMap) jwt.getClaims().get("realm_access")).get("roles");
            roles = roles.stream()
                    .map(String::toString)
                    .collect(Collectors.toList());

            user.setRoles(roles);
            log.info("user = {}", user);


        } catch (Exception e) {
            log.info("Failed to decode the token");
        }
    }
}
