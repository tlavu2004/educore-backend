package com.tlavu.educore.auth.user.infrastructure.persistence.jpa.mapper;

import com.tlavu.educore.auth.user.domain.entity.User;
import com.tlavu.educore.auth.user.domain.valueobject.Email;
import com.tlavu.educore.auth.user.domain.valueobject.HashedPassword;
import com.tlavu.educore.auth.user.infrastructure.persistence.jpa.entity.UserJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserJpaMapper {

    UserJpaMapper INSTANCE = Mappers.getMapper(UserJpaMapper.class);

    @Mapping(target = "email", source = "email")
    @Mapping(target = "passwordHash", source = "passwordHash")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "firstLogin", source = "firstLogin")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "lastLoginAt", source = "lastLoginAt")
    @Mapping(target = "createdBy", source = "createdBy")
    User toDomain(UserJpaEntity entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email.value")
    @Mapping(target = "passwordHash", source = "hashedPassword.hashedValue")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "firstLogin", source = "firstLogin")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "lastLoginAt", source = "lastLoginAt")
    @Mapping(target = "createdBy", source = "createdBy")
    UserJpaEntity toEntity(User domain);

    default Email mapToEmail(String email) {
        return email == null ? null : Email.of(email);
    }

    default HashedPassword mapToHashedPassword(String hashed) {
        return hashed == null ? null : HashedPassword.fromHashed(hashed);
    }

    default String mapFromHashedPassword(HashedPassword hp) {
        return hp == null ? null : hp.hashedValue();
    }

}

