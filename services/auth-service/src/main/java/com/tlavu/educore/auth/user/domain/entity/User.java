package com.tlavu.educore.auth.user.domain.entity;

import com.tlavu.educore.auth.domain.entities.Role;
import com.tlavu.educore.auth.shared.domain.entity.BaseDomainEntity;
import com.tlavu.educore.auth.user.domain.enums.UserRole;
import com.tlavu.educore.auth.user.domain.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseDomainEntity<UUID> {

    private String email;
    private String passwordHash;
    private boolean firstLogin;
    private UserStatus status;
    private UserRole role;
    private Instant lastLoginAt;
    private UUID createdById;

    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }

    public boolean isPendingActivation() {
        return status == UserStatus.PENDING_ACTIVATION;
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
        this.firstLogin = true;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.status = UserStatus.INACTIVE;
        this.updatedAt = Instant.now();
    }

    public void completeFirstLogin() {
        this.firstLogin = false;
        this.lastLoginAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void updatePassword(String newPasswordHash) {
        this.passwordHash = newPasswordHash;
        this.updatedAt = Instant.now();
    }
}

