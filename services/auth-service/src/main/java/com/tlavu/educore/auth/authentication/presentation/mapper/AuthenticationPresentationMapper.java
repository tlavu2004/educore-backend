package com.tlavu.educore.auth.authentication.presentation.mapper;

import org.mapstruct.Mapper;
import com.tlavu.educore.auth.authentication.presentation.dto.request.LoginRequest;
import com.tlavu.educore.auth.authentication.presentation.dto.request.RefreshTokenRequest;
import com.tlavu.educore.auth.authentication.application.usecase.command.LoginCommand;
import com.tlavu.educore.auth.authentication.application.usecase.command.RefreshTokenCommand;

@Mapper(componentModel = "spring")
public interface AuthenticationPresentationMapper {

    LoginCommand toLoginCommand(LoginRequest req);

    RefreshTokenCommand toRefreshCommand(RefreshTokenRequest req);
}

