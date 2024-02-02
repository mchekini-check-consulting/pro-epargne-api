package com.checkconsulting.proepargne.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
@Scope(value = SCOPE_REQUEST, proxyMode = TARGET_CLASS)
public class User {

    private String userName;
    private List<String> roles;
    private boolean authenticated;
    private String email;
}
