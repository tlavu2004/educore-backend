package com.tlavu.educore.auth.authentication.infrastructure.token.activation;

import com.tlavu.educore.auth.shared.application.service.EmailService;
import org.springframework.stereotype.Component;

@Component
public class ActivationEmailSender {

    private final ActivationLinkBuilder linkBuilder;
    private final JwtActivationTokenService tokenService;
    private final EmailService emailService;

    public ActivationEmailSender(ActivationLinkBuilder linkBuilder, JwtActivationTokenService tokenService, EmailService emailService) {
        this.linkBuilder = linkBuilder;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    public void sendActivation(String toEmail, String username, String defaultPassword) {
        String token = tokenService.createActivationToken(java.util.UUID.randomUUID());
        String link = linkBuilder.build(token);

        emailService.sendActivationEmail(toEmail, username, token, defaultPassword);
    }
}

