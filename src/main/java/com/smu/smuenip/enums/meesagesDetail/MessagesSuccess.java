package com.smu.smuenip.enums.meesagesDetail;

import com.smu.smuenip.enums.Messages;

public enum MessagesSuccess implements Messages {

    SIGNUP_SUCCESS("회원가입 성공"),
    LOGIN_USER_SUCCESS("유저 로그인 성공"),
    LOGIN_ADMIN_SUCCESS("관리자 로그인 성공"),
    UPLOAD_SUCCESS("업로드 성공");

    private final String message;

    MessagesSuccess(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
