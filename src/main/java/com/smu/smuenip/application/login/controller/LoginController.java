package com.smu.smuenip.application.login.controller;

import com.smu.smuenip.Infrastructure.config.redis.CustomUserDetails;
import com.smu.smuenip.application.login.dto.LoginRequestDto;
import com.smu.smuenip.application.login.dto.LoginResponseDto;
import com.smu.smuenip.application.login.dto.LoginResult;
import com.smu.smuenip.application.login.dto.ResponseDto;
import com.smu.smuenip.application.login.dto.UserRequestDto;
import com.smu.smuenip.domain.user.serivce.UserAuthService;
import com.smu.smuenip.enums.Role;
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
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        LoginResult loginResult = userAuthService.login(requestDto);

        LoginResponseDto loginResponseDto = new LoginResponseDto(
            true,
            loginResult.getRole() == Role.ROLE_USER ? MessagesSuccess.LOGIN_USER_SUCCESS
                : MessagesSuccess.LOGIN_ADMIN_SUCCESS,
            loginResult.getToken()
        );

        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }

    @GetMapping("/token")
    public String getAccessToken(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return customUserDetails.getAccessToken();
    }
}
