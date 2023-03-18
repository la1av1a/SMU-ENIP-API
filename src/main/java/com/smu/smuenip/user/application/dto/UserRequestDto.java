package com.smu.smuenip.user.application.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserRequestDto {

    private String userId;
    private String password;
    private String phoneNumber;
    private String email;
    private LocalDate birth;
}
