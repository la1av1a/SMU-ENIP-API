package com.smu.smuenip.application.auth.controller;

import com.smu.smuenip.Infrastructure.config.filters.CustomUserDetails;
import com.smu.smuenip.application.auth.dto.TokenResponse;
import com.smu.smuenip.application.auth.dto.UserLoginRequestDto;
import com.smu.smuenip.application.auth.dto.UserRequestDto;
import com.smu.smuenip.enums.Messages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Api(tags = "회원가입, 로그인관련")
public interface LoginControllerSwagger {

    @ApiOperation(value = "회원가입", notes = "사용자를 생성합니다")
    ResponseEntity<Messages> signUp(@RequestBody UserRequestDto requestDto);

    @ApiOperation(value = "로그인", notes = "사용자 ID로 사용자 정보를 조회합니다?")
    ResponseEntity<TokenResponse> login(@RequestBody UserLoginRequestDto requestDto);

    @ApiOperation(value = "로그인 테스트", notes = "로그인된 사용자 정보 리턴되어야함")
    String loginTest(@AuthenticationPrincipal CustomUserDetails user);
}
