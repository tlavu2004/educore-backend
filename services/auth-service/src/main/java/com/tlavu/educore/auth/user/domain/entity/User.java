package com.tlavu.educore.auth.user.domain.entity;

import com.tlavu.educore.auth.shared.domain.entity.BaseDomainEntity;
import com.tlavu.educore.auth.user.domain.enums.UserRole;
import com.tlavu.educore.auth.user.domain.enums.UserStatus;
import com.tlavu.educore.auth.user.domain.valueobject.Email;
import com.tlavu.educore.auth.user.domain.valueobject.HashedPassword;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class User extends BaseDomainEntity<UUID> {

    private final Email email;                  // Mìght need to be mutable in future for email change feature
    private HashedPassword hashedPassword;
    private final String fullName;              // Mìght need to be mutable in future for name change feature
    private UserStatus status;
    private final UserRole role;                // Might need to be mutable in future for role change feature
    private Instant lastLoginAt;
    private final UUID createdById;

    public User(
            UUID id,
            Email email,
            HashedPassword hashedPassword,
            String fullName,
            UserRole role,
            UserStatus status,
            Instant createdAt,
            Instant updatedAt,
            UUID createdById
    ) {
        this.id = id;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.fullName = fullName;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdById = createdById;
    }

    public static User createNew(
            Email email,
            HashedPassword hashedPassword,
            String fullName,
            UserRole role,
            UUID createdById
    ) {
        Objects.requireNonNull(email, "email cannot be null");
        Objects.requireNonNull(hashedPassword, "hashedPassword cannot be null");
        Objects.requireNonNull(fullName, "fullName cannot be null");
        Objects.requireNonNull(role, "role cannot be null");
        Objects.requireNonNull(createdById, "createdById cannot be null");

        Instant now = Instant.now();
        return new User(
                UUID.randomUUID(),
                email,
                hashedPassword,
                fullName,
                role,
                UserStatus.PENDING_ACTIVATION,
                now,
                now,
                createdById
        );
    }

    public static User reconstruct(
            UUID id,
            Email email,
            HashedPassword hashedPassword,
            String fullName,
            UserRole role,
            UserStatus status,
            Instant createdAt,
            Instant updatedAt,
            Instant lastLoginAt,
            UUID createdById
    ) {
        User user = new User(
                id,
                email,
                hashedPassword,
                fullName,
                role,
                status,
                createdAt,
                updatedAt,
                createdById
        );
        user.lastLoginAt = lastLoginAt;
        return user;
    }

    public boolean isPendingActivation() {
        return status == UserStatus.PENDING_ACTIVATION;
    }

    public boolean isActivated() {
        return status == UserStatus.ACTIVATED;
    }

    public boolean isSuspended() {
        return status == UserStatus.SUSPENDED;
    }

    public void activateFirstLogin() {
        this.status = UserStatus.ACTIVATED;
        this.lastLoginAt = Instant.now();
    }

    public void recordLogin() {
        this.lastLoginAt = Instant.now();
    }

    public void updatePassword(HashedPassword newHashedPassword) {
        this.hashedPassword = newHashedPassword;
        this.updatedAt = Instant.now();
    }

    public void activate() {
        this.status = UserStatus.ACTIVATED;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.status = UserStatus.SUSPENDED;
        this.updatedAt = Instant.now();
    }
}

