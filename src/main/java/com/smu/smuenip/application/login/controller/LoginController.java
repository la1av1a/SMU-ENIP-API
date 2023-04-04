package com.smu.smuenip.application.login.controller;

import com.smu.smuenip.Infrastructure.config.redis.TokenInfo;
import com.smu.smuenip.application.login.dto.ResponseDto;
import com.smu.smuenip.application.login.dto.UserLoginRequestDto;
import com.smu.smuenip.application.login.dto.UserRequestDto;
import com.smu.smuenip.domain.user.serivce.UserAuthService;
import com.smu.smuenip.enums.meesagesDetail.MessagesSuccess;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/user")
@RequiredArgsConstructor
@RestController
public class LoginController implements LoginControllerSwagger {

    private final UserAuthService userAuthService;

    @Override
    @PostMapping("/signUp")
    public ResponseEntity<ResponseDto> signUp(@RequestBody UserRequestDto requestDto) {
        userAuthService.createUser(requestDto);

        return new ResponseEntity<>(
            new ResponseDto(true, MessagesSuccess.SIGNUP_SUCCESS.getMessage()), HttpStatus.OK);
    }


    @Override
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequestDto requestDto) {
        String token = userAuthService.login(requestDto);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/token")
    public String getAccessToken(@AuthenticationPrincipal TokenInfo tokenInfo) {
        return tokenInfo.getAccessToken();
    }
}
