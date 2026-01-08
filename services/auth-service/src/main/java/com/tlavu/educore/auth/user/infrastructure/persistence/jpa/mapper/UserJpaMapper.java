package com.tlavu.educore.auth.user.infrastructure.persistence.jpa.mapper;

import com.tlavu.educore.auth.user.domain.entity.User;
import com.tlavu.educore.auth.user.domain.enums.UserRole;
import com.tlavu.educore.auth.user.domain.valueobject.Email;
import com.tlavu.educore.auth.user.domain.valueobject.HashedPassword;
import com.tlavu.educore.auth.user.domain.valueobject.UserId;
import com.tlavu.educore.auth.user.infrastructure.persistence.jpa.entity.UserJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserJpaMapper {

    UserJpaMapper INSTANCE = Mappers.getMapper(UserJpaMapper.class);

    // Use a manual implementation for toDomain to call the domain factory (reconstruct)
    default User toDomain(UserJpaEntity entity) {
        if (entity == null) return null;

        UserId id = entity.getId() == null ? null : UserId.of(entity.getId());
        Email email = entity.getEmail() == null ? null : Email.of(entity.getEmail());
        HashedPassword hp = entity.getPasswordHash() == null ? null : HashedPassword.fromHashed(entity.getPasswordHash());
        UserId createdBy = entity.getCreatedById() == null ? null : UserId.of(entity.getCreatedById());

        // NOTE: The persisted entity doesn't have a role column yet; default to ADMIN when reconstructing.
        UserRole role = UserRole.ADMIN;

        return User.reconstruct(
                id,
                email,
                hp,
                entity.getFullName(),
                role,
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getLastLoginAt(),
                createdBy
        );
    }

    // Map domain -> JPA entity using MapStruct (helpers below will be used)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "passwordHash", source = "hashedPassword.hashedValue")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "lastLoginAt", source = "lastLoginAt")
    @Mapping(target = "createdById", source = "createdById")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    UserJpaEntity toEntity(User domain);

    // Helpers for MapStruct conversions
    default Email mapToEmail(String email) {
        return email == null ? null : Email.of(email);
    }

    default HashedPassword mapToHashedPassword(String hashed) {
        return hashed == null ? null : HashedPassword.fromHashed(hashed);
    }

    default String mapFromHashedPassword(HashedPassword hp) {
        return hp == null ? null : hp.hashedValue();
    }

    default UUID map(UserId id) {
        return id == null ? null : id.value();
    }

    default UserId map(UUID id) {
        return id == null ? null : UserId.of(id);
    }

    default String mapFromEmail(Email email) {
        return email == null ? null : email.toString();
    }

}
