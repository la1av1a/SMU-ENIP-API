package com.smu.smuenip.application.login.dto;

import com.smu.smuenip.domain.user.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserInfoResponseDto {

    private final Long id;
    private final String nickName;
    private final int score;
    private final String profileImageUrl;


    public UserInfoResponseDto(User user) {
        this.id = user.getUserId();
        this.nickName = user.getNickName();
        this.score = user.getScore();
        this.profileImageUrl = user.getProfileImageUrl();
    }
}
