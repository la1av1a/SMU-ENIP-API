package com.smu.smuenip.user.application.enums;

public enum MessagesFail implements Messages {

    USER_EXISTS("유저가 이미 존재합니다"),
    USER_NOT_FOUND("ID 혹은 비밀번호를 확인하세요");

    private final String message;

    MessagesFail(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
