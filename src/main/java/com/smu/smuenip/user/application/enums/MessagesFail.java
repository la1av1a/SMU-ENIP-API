package com.smu.smuenip.user.application.enums;

public enum MessagesFail implements Messages {

    USER_EXISTS("유저가 이미 존재합니다");

    private final String message;

    MessagesFail(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
