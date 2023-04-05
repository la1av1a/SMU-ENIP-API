package com.smu.smuenip.application.login.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequestDto {

    private String loginId;
    private String password;

    @Builder
    public UserLoginRequestDto(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
