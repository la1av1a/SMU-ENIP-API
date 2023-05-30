package com.smu.smuenip.application.login.controller;

import com.smu.smuenip.application.login.dto.LoginResult;
import com.smu.smuenip.application.login.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Api(tags = "소셜 로그인")
public interface OauthLoginControllerSwagger {

    @ApiOperation(value = "카카오 로그인")
    public ResponseEntity<ResponseDto<LoginResult>> kakaoLogin(@RequestParam("code") String code);
}
