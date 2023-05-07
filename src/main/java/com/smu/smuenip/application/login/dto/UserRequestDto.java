package com.smu.smuenip.application.login.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequestDto {

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
    @NotEmpty
    private String email;

    @Builder
    public UserRequestDto(String loginId, String password, String email) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
    }
}
