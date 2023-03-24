package com.smu.smuenip.application.user.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserRequestDto {

    private String userId;
    private String password;
    private String phoneNumber;
    private String email;
    private LocalDate birth;

    @Builder
    public UserRequestDto(String userId, String password, String phoneNumber, String email,
        LocalDate birth) {
        this.userId = userId;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birth = birth;
    }
}
