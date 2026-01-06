package com.tlavu.educore.auth.user.domain.entity;

import com.tlavu.educore.auth.shared.domain.entity.BaseDomainEntity;
import com.tlavu.educore.auth.user.domain.enums.UserRole;
import com.tlavu.educore.auth.user.domain.enums.UserStatus;
import com.tlavu.educore.auth.user.domain.exception.UserAlreadyActivatedException;
import com.tlavu.educore.auth.user.domain.exception.UserNotActivatedException;
import com.tlavu.educore.auth.user.domain.exception.UserNotPendingActivationException;
import com.tlavu.educore.auth.user.domain.valueobject.Email;
import com.tlavu.educore.auth.user.domain.valueobject.HashedPassword;
import com.tlavu.educore.auth.user.domain.valueobject.UserId;
import lombok.Getter;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class User extends BaseDomainEntity<UserId> {

    private Email email;
    private HashedPassword hashedPassword;
    private String fullName;
    private UserStatus status;
    private UserRole role;
    private Instant lastLoginAt;
    private UserId createdById;

    private User() {
        // For reconstruction purposes
    }

    protected User(
            UserId id,
            Email email,
            HashedPassword hashedPassword,
            String fullName,
            UserRole role,
            UserStatus status,
            UserId createdById,
            Instant now
    ) {
        this.id = id;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.fullName = fullName;
        this.role = role;
        this.status = status;
        this.createdById = createdById;
        this.markCreated(now);
    }

    public static User createNew(
            Email email,
            HashedPassword hashedPassword,
            String fullName,
            UserRole role,
            UserId createdById,
            Clock clock
    ) {
        Objects.requireNonNull(email, "email cannot be null");
        Objects.requireNonNull(hashedPassword, "hashedPassword cannot be null");
        Objects.requireNonNull(fullName, "fullName cannot be null");
        Objects.requireNonNull(role, "role cannot be null");
        Objects.requireNonNull(createdById, "createdById cannot be null");
        Objects.requireNonNull(clock, "clock cannot be null");

        Instant now = Instant.now(clock);
        return new User(
                UserId.of(UUID.randomUUID()),
                email,
                hashedPassword,
                fullName,
                role,
                UserStatus.PENDING_ACTIVATION,
                createdById,
                now
        );
    }

    public static User reconstruct(
            UserId id,
            Email email,
            HashedPassword hashedPassword,
            String fullName,
            UserRole role,
            UserStatus status,
            Instant createdAt,
            Instant updatedAt,
            Instant lastLoginAt,
            UserId createdById
    ) {
        User user = new User();

        user.id = id;
        user.email = email;
        user.hashedPassword = hashedPassword;
        user.fullName = fullName;
        user.role = role;
        user.status = status;
        user.createdAt = createdAt;
        user.updatedAt = updatedAt;
        user.lastLoginAt = lastLoginAt;
        user.createdById = createdById;

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

    public void recordLogin(Clock clock) {
        Objects.requireNonNull(clock, "clock cannot be null");

        if (this.status != UserStatus.ACTIVATED) {
            throw new UserNotActivatedException("Account is not activated");
        }

        Instant now = Instant.now(clock);
        this.lastLoginAt = now;
        this.markUpdated(now);
    }

    public void updatePassword(HashedPassword newHashedPassword, Clock clock) {
        Objects.requireNonNull(newHashedPassword, "newHashedPassword cannot be null");
        Objects.requireNonNull(clock, "clock cannot be null");

        Instant now = Instant.now(clock);
        this.hashedPassword = newHashedPassword;
        this.markUpdated(now);
    }

    public void activate(Clock clock) {
        Objects.requireNonNull(clock, "clock cannot be null");

        if (this.status != UserStatus.PENDING_ACTIVATION) {
            throw new UserNotPendingActivationException(
                    "Cannot complete first login: user is not pending activation"
            );
        }

        if (this.lastLoginAt != null) {
            throw new UserAlreadyActivatedException(
                    "First login already completed"
            );
        }

        Instant now = Instant.now(clock);
        this.status = UserStatus.ACTIVATED;
        this.lastLoginAt = now;
        this.markUpdated(now);
    }

    public void suspend(Clock clock) {
        Objects.requireNonNull(clock, "clock cannot be null");

        Instant now = Instant.now(clock);
        this.status = UserStatus.SUSPENDED;
        this.markUpdated(now);
    }
}
