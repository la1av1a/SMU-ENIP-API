package com.smu.smuenip.application.login.controller;

import com.smu.smuenip.application.login.dto.LoginRequestDto;
import com.smu.smuenip.application.login.dto.ResponseDto;
import com.smu.smuenip.application.login.dto.UserRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;

@Api(tags = "회원가입, 로그인관련")
public interface LoginControllerSwagger {

    @ApiOperation(value = "회원가입", notes = "사용자를 생성합니다")
    ResponseDto signUp(
            @RequestBody UserRequestDto requestDto);

    @ApiOperation(value = "로그인", notes = "사용자 ID로 사용자 정보를 조회합니다")
    ResponseDto login(@RequestBody LoginRequestDto requestDto);
}
