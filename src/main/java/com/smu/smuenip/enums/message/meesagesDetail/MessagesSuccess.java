package com.smu.smuenip.enums.message.meesagesDetail;

import com.smu.smuenip.enums.message.Messages;

public enum MessagesSuccess implements Messages {

    SIGNUP_SUCCESS("회원가입 성공"),
    LOGIN_USER_SUCCESS("유저 로그인 성공"),
    LOGIN_ADMIN_SUCCESS("관리자 로그인 성공"),
    UPLOAD_SUCCESS("업로드 성공"),
    COMMENT_SUCCESS("코멘트 달기 성공"),
    DELETE_USER_SUCCESS("유저 삭제 성공"),
    //유저 조회 성공
    RETRIEVE_USER_INFO_SUCCESS("유저 조회 성공"),
    APPROVE_SUCCESS("승인 성공"),
    REJECT_SUCCESS("거절 성공");

    private final String message;

    MessagesSuccess(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
