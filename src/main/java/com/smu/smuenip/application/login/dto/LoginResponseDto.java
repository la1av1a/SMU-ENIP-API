package com.smu.smuenip.application.login.dto;

import com.smu.smuenip.enums.message.Messages;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponseDto {

    private boolean success;
    private Messages messages;
    private String token;

    public LoginResponseDto(boolean success, Messages messages, String token) {
        this.success = success;
        this.messages = messages;
        this.token = token;
    }
}
