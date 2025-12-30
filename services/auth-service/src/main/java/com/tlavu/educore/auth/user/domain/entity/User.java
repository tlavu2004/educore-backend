package com.tlavu.educore.auth.user.domain.entity;

import com.tlavu.educore.auth.shared.domain.entity.BaseDomainEntity;
import com.tlavu.educore.auth.user.domain.enums.UserRole;
import com.tlavu.educore.auth.user.domain.enums.UserStatus;
import com.tlavu.educore.auth.user.domain.valueobject.Email;
import com.tlavu.educore.auth.user.domain.valueobject.HashedPassword;
import lombok.Getter;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class User extends BaseDomainEntity<UUID> {

    // TODO: UpdateUserProfileRequest
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
            UUID createdById,
            Clock clock
    ) {
        Objects.requireNonNull(email, "email cannot be null");
        Objects.requireNonNull(hashedPassword, "hashedPassword cannot be null");
        Objects.requireNonNull(fullName, "fullName cannot be null");
        Objects.requireNonNull(role, "role cannot be null");
        Objects.requireNonNull(createdById, "createdById cannot be null");
        Objects.requireNonNull(clock, "clock cannot be null");

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

    public void completeFirstLogin(Clock clock) {
        Objects.requireNonNull(clock, "clock cannot be null");

        if (this.status != UserStatus.PENDING_ACTIVATION) {
            throw new IllegalStateException(
                    "Cannot complete first login: user is not pending activation"
            );
        }

        if (this.lastLoginAt != null) {
            throw new IllegalStateException("First login already completed");
        }

        Instant now = Instant.now(clock);
        this.status = UserStatus.ACTIVATED;
        this.lastLoginAt = now;
        this.updatedAt = now;
    }

    public void recordLogin(Clock clock) {
        Objects.requireNonNull(clock, "clock cannot be null");

        if (this.status != UserStatus.ACTIVATED) {
            throw new IllegalStateException("Account is not activated");
        }

        Instant now = Instant.now(clock);
        this.lastLoginAt = now;
        this.updatedAt = now;
    }

    public void updatePassword(HashedPassword newHashedPassword, Clock clock) {
        Objects.requireNonNull(newHashedPassword, "newHashedPassword cannot be null");
        Objects.requireNonNull(clock, "clock cannot be null");

        this.hashedPassword = newHashedPassword;
        this.updatedAt = Instant.now(clock);
    }

    public void activate(Clock clock) {
        Objects.requireNonNull(clock, "clock cannot be null");

        this.status = UserStatus.ACTIVATED;
        this.updatedAt = Instant.now();
    }

    public void deactivate(Clock clock) {
        Objects.requireNonNull(clock, "clock cannot be null");

        this.status = UserStatus.SUSPENDED;
        this.updatedAt = Instant.now();
    }
}
