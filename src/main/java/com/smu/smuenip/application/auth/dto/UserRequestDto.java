package com.smu.smuenip.application.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequestDto {

    private String loginId;
    private String password;
    private String email;

    @Builder
    public UserRequestDto(String loginId, String password, String email) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
    }
}
