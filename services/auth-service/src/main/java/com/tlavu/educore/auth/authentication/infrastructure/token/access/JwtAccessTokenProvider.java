package com.tlavu.educore.auth.authentication.infrastructure.token.access;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtAccessTokenProvider {

    private final byte[] secretBytes;
    private final String issuer;
    private final long expirationSeconds;

    public JwtAccessTokenProvider(
            @Value("${auth.jwt.secret}") String secret,
            @Value("${auth.jwt.issuer:educore-auth}") String issuer,
            @Value("${auth.jwt.access-expiration-sec:900}") long expirationSeconds
    ) {
        this.secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.issuer = issuer;
        this.expirationSeconds = expirationSeconds;
    }

    public String createToken(UUID userId, String email, String role) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expirationSeconds);

        return Jwts.builder()
                .subject(userId.toString())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .claim("email", email)
                .claim("role", role)
                .signWith(Keys.hmacShaKeyFor(secretBytes))
                .compact();
    }

    public boolean validate(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public UUID extractUserId(String token) {
        Claims claims = parseClaims(token).getPayload();
        return UUID.fromString(claims.getSubject());
    }

    public Instant extractExpiration(String token) {
        Date exp = parseClaims(token).getPayload().getExpiration();
        return exp.toInstant();
    }

    public Jws<Claims> parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretBytes))
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token);
    }
}
