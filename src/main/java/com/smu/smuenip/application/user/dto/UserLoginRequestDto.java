package com.smu.smuenip.application.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserLoginRequestDto {

    private final String userId;
    private final String password;

}
