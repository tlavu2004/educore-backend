package com.tlavu.educore.auth.user.application.mapper;

import com.tlavu.educore.auth.user.application.dto.response.UserResponse;
import com.tlavu.educore.auth.user.domain.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "email", expression = "java(user.getEmail().getValue())")
    @Mapping(target = "role", expression = "java(user.getRole().name())")
    @Mapping(target = "status", expression = "java(user.getStatus().name())")
    @Mapping(target = "lastLoginAt", source = "lastLoginAt")
    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);
}