package com.smu.smuenip.application.login.dto;

import com.smu.smuenip.enums.Role;
import lombok.Getter;

@Getter
public class LoginResult {

    private String token;
    private Role role;
    private String nickname;
    private String profileImageUrl;
    private int score;

    public LoginResult(String token, Role role, String nickname, String profileImageUrl, int score) {
        this.token = token;
        this.role = role;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.score = score;
    }
}
