package com.smu.smuenip.application.login.dto;

import com.smu.smuenip.enums.Role;
import lombok.Getter;

@Getter
public class LoginResult {

    private String token;
    private Role role;
    
    public LoginResult(String token, Role role) {
        this.token = token;
        this.role = role;
    }
}
