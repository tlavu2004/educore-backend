package com.tlavu.educore.auth.authentication.presentation.controller;

import com.tlavu.educore.auth.authentication.presentation.dto.request.LoginRequest;
import com.tlavu.educore.auth.authentication.presentation.dto.request.RefreshTokenRequest;
import com.tlavu.educore.auth.authentication.presentation.dto.response.AuthResult;
import com.tlavu.educore.auth.authentication.application.usecase.command.LoginCommand;
import com.tlavu.educore.auth.authentication.application.usecase.command.RefreshTokenCommand;
import com.tlavu.educore.auth.authentication.application.usecase.handler.LoginHandler;
import com.tlavu.educore.auth.authentication.application.usecase.handler.RefreshTokenHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final LoginHandler loginHandler;
    private final RefreshTokenHandler refreshTokenHandler;

    public AuthenticationController(LoginHandler loginHandler,
                                    RefreshTokenHandler refreshTokenHandler) {
        this.loginHandler = loginHandler;
        this.refreshTokenHandler = refreshTokenHandler;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResult> login(@Validated @RequestBody LoginRequest req) {
        LoginCommand cmd = new LoginCommand(req.email(), req.password());
        AuthResult result = loginHandler.handle(cmd);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResult> refresh(@Validated @RequestBody RefreshTokenRequest req) {
        RefreshTokenCommand cmd = new RefreshTokenCommand(req.refreshTokens());
        AuthResult result = refreshTokenHandler.handler(cmd);
        return ResponseEntity.ok(result);
    }
}
