package com.smu.smuenip.user.application.enums.meesagesDetail;

import com.smu.smuenip.user.application.enums.Messages;

public enum MessagesSuccess implements Messages {

    SIGNUP_SUCCESS("회원가입 성공"),
    LOGIN_SUCCESS("로그인 성공");

    private final String message;

    MessagesSuccess(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
