package com.smu.smuenip.application.auth.controller;

import com.smu.smuenip.application.auth.dto.TokenResponse;
import com.smu.smuenip.application.auth.dto.UserLoginRequestDto;
import com.smu.smuenip.application.auth.dto.UserRequestDto;
import com.smu.smuenip.domain.auth.service.UserAuthService;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.enums.Messages;
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
    public ResponseEntity<Messages> signUp(@RequestBody UserRequestDto requestDto) {
        userAuthService.createUser(requestDto);

        return new ResponseEntity<>(MessagesSuccess.SIGNUP_SUCCESS, HttpStatus.OK);
    }


    @Override
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserLoginRequestDto requestDto) {
        TokenResponse tokenResponse = userAuthService.login(requestDto);

        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    @Override
    @GetMapping("/loginTest")
    public String loginTest(@AuthenticationPrincipal User user) {
        return user.getUserId();
    }

}
