package com.tlavu.educore.auth.user.application.mapper;

import com.tlavu.educore.auth.user.application.dto.response.UserProfileResponse;
import com.tlavu.educore.auth.user.application.dto.response.UserResponse;
import com.tlavu.educore.auth.user.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "email", expression = "java(user.getEmail().value())")
    @Mapping(target = "role", expression = "java(user.getRole().name())")
    @Mapping(target = "status", expression = "java(user.getStatus().name())")
    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);

    @Mapping(target = "email", expression = "java(user.getEmail().value())")
    UserProfileResponse toProfileResponse(User user);

    List<UserProfileResponse> toProfileResponseList(List<User> users);
}