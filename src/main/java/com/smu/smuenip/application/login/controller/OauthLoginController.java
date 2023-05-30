package com.smu.smuenip.application.login.controller;

import com.smu.smuenip.application.login.dto.LoginResult;
import com.smu.smuenip.application.login.dto.ResponseDto;
import com.smu.smuenip.domain.user.serivce.UserAuthService;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesSuccess;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user/oauth/login")
@RestController
@RequiredArgsConstructor
public class OauthLoginController implements OauthLoginControllerSwagger {

    private final UserAuthService userAuthService;

    @Override
    @GetMapping("/kakao")
    public ResponseEntity<ResponseDto<LoginResult>> kakaoLogin(@RequestParam("code") String code) {
        return new ResponseEntity<>(new ResponseDto<>(userAuthService.kakaoLogin(code),
            MessagesSuccess.LOGIN_USER_SUCCESS.getMessage()), HttpStatus.OK);
    }
}
