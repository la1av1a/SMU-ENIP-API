package com.smu.smuenip.application.login.controller;

import com.smu.smuenip.application.login.dto.LoginRequestDto;
import com.smu.smuenip.application.login.dto.LoginResult;
import com.smu.smuenip.application.login.dto.ResponseDto;
import com.smu.smuenip.application.login.dto.UserRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Api(tags = "회원가입, 로그인관련")
public interface LoginControllerSwagger {

    @ApiOperation(value = "회원가입", notes = "사용자를 생성합니다")
    ResponseEntity<ResponseDto<Void>> signUp(
            @RequestBody UserRequestDto requestDto);

    @ApiOperation(value = "로그인", notes = "사용자 ID로 사용자 정보를 조회합니다")
    ResponseEntity<ResponseDto<LoginResult>> login(@RequestBody LoginRequestDto requestDto);
}
