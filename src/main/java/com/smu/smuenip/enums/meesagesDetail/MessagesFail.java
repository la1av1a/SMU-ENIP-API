package com.smu.smuenip.enums.meesagesDetail;

import com.smu.smuenip.enums.Messages;

public enum MessagesFail implements Messages {

    USER_EXISTS("유저가 이미 존재합니다"),
    USER_NOT_FOUND("유저를 찾을 수 없습니다"),
    UNAUTHORIZED("권한 정보가 없습니다"),
    UNEXPECTED_ERROR("서버에 예상치 못한 에러가 발생했습니다"),
    IMAGE_UPLOAD_FAIL("이미지 업로드에 실패했습니다. 다시 시도해주세요."),
    RECEIPT_NOT_FOUND("영수증 정보를 찾을 수 없습니다");

    private final String message;

    MessagesFail(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
