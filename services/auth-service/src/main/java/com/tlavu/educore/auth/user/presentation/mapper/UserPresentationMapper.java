package com.tlavu.educore.auth.user.presentation.mapper;

import com.tlavu.educore.auth.user.presentation.dto.request.CreateUserRequest;
import com.tlavu.educore.auth.user.application.usecase.command.CreateUserCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserPresentationMapper {

    CreateUserCommand toCreateUserCommand(CreateUserRequest req);
}

