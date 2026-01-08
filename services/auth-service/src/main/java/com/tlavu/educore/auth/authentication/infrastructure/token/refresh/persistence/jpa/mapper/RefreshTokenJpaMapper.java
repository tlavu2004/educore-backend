package com.tlavu.educore.auth.authentication.infrastructure.token.refresh.persistence.jpa.mapper;

import com.tlavu.educore.auth.authentication.domain.entity.RefreshToken;
import com.tlavu.educore.auth.authentication.domain.valueobject.RefreshTokenValue;
import com.tlavu.educore.auth.authentication.infrastructure.token.refresh.persistence.jpa.entity.RefreshTokenJpaEntity;
import com.tlavu.educore.auth.user.domain.valueobject.UserId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Optional;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RefreshTokenJpaMapper {

    @Mapping(source = "refreshTokenValue", target = "token", qualifiedByName = "refreshTokenValueToString")
    @Mapping(source = "userId", target = "userId", qualifiedByName = "userIdToUuid")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "expiresAt", target = "expiresAt")
    @Mapping(source = "revokedAt", target = "revokedAt")
    RefreshTokenJpaEntity toEntity(RefreshToken token);

    @Mapping(source = "token", target = "refreshTokenValue", qualifiedByName = "stringToRefreshTokenValue")
    @Mapping(source = "userId", target = "userId", qualifiedByName = "uuidToUserId")
    RefreshToken toDomain(RefreshTokenJpaEntity entity);

    default Optional<RefreshToken> toDomain(Optional<RefreshTokenJpaEntity> e) {
        return e.map(this::toDomain);
    }

    @Named("refreshTokenValueToString")
    static String refreshTokenValueToString(RefreshTokenValue v) {
        return v == null ? null : v.toString();
    }

    @Named("stringToRefreshTokenValue")
    static RefreshTokenValue stringToRefreshTokenValue(String s) {
        return s == null ? null : RefreshTokenValue.of(s);
    }

    @Named("userIdToUuid")
    static UUID userIdToUuid(UserId userId) {
        return userId == null ? null : userId.value();
    }

    @Named("uuidToUserId")
    static UserId uuidToUserId(UUID id) {
        return id == null ? null : UserId.of(id);
    }
}
