package com.smu.smuenip.application.login.controller;

import com.smu.smuenip.application.login.dto.LoginRequestDto;
import com.smu.smuenip.application.login.dto.LoginResult;
import com.smu.smuenip.application.login.dto.ResponseDto;
import com.smu.smuenip.application.login.dto.UserRequestDto;
import com.smu.smuenip.domain.user.serivce.UserAuthService;
import com.smu.smuenip.enums.Role;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesSuccess;
import com.smu.smuenip.infrastructure.config.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequestMapping("/user")
@RequiredArgsConstructor
@RestController
public class LoginController implements LoginControllerSwagger {

    private final UserAuthService userAuthService;

    @Override
    @PostMapping("/signUp")
    public ResponseDto<Void> signUp(@RequestBody @Valid UserRequestDto requestDto) {
        userAuthService.createUser(requestDto);

        return new ResponseDto<>(null, true, MessagesSuccess.SIGNUP_SUCCESS.getMessage());
    }

    @Override
    @PostMapping("/login")
    public ResponseDto<LoginResult> login(@RequestBody @Valid LoginRequestDto requestDto) {
        LoginResult loginResult = userAuthService.login(requestDto);

        String message = loginResult.getRole() == Role.ROLE_USER ? MessagesSuccess.LOGIN_USER_SUCCESS.toString()
                : MessagesSuccess.LOGIN_ADMIN_SUCCESS.toString();

        return new ResponseDto<>(loginResult, true, message);
    }

    @GetMapping("/token")
    public String getAccessToken(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return customUserDetails.getAccessToken();
    }
}
