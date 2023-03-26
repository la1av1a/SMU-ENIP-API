package com.smu.smuenip.application.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequestDto {

    private String loginId;
    private String password;
    private String phoneNumber;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birth;

    @Builder
    public UserRequestDto(String loginId, String password, String phoneNumber, String email,
        LocalDate birth) {
        this.loginId = loginId;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birth = birth;
    }
}
