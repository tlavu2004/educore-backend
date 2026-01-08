package com.tlavu.educore.auth.authentication.infrastructure.token.activation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ActivationLinkBuilder {

    private final String baseUrl;

    public ActivationLinkBuilder(@Value("${auth.activation.base-url:http://localhost:8080}") String baseUrl) {
        this.baseUrl = baseUrl.replaceAll("/$", "");
    }

    public String build(String token) {
        return String.format("%s/activate?token=%s", baseUrl, token);
    }
}

