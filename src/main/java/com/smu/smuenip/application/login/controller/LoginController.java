package com.smu.smuenip.application.login.controller;

import com.smu.smuenip.application.login.dto.*;
import com.smu.smuenip.domain.user.serivce.UserAuthService;
import com.smu.smuenip.domain.user.serivce.UserService;
import com.smu.smuenip.enums.Role;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesSuccess;
import com.smu.smuenip.infrastructure.config.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequestMapping("/user")
@RequiredArgsConstructor
@RestController
public class LoginController implements LoginControllerSwagger {

    private final UserAuthService userAuthService;
    private final UserService userService;

    @Override
    @PostMapping("/signUp")
    public ResponseEntity<ResponseDto<Void>> signUp(@RequestBody @Valid UserRequestDto requestDto) {
        userAuthService.createUser(requestDto);

        return new ResponseEntity<>(new ResponseDto<>(null, MessagesSuccess.SIGNUP_SUCCESS.getMessage()), HttpStatus.OK);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<LoginResult>> login(@RequestBody @Valid LoginRequestDto requestDto) {
        LoginResult loginResult = userAuthService.login(requestDto);

        String message = loginResult.getRole() == Role.ROLE_USER ? MessagesSuccess.LOGIN_USER_SUCCESS.toString()
                : MessagesSuccess.LOGIN_ADMIN_SUCCESS.toString();

        return new ResponseEntity<>(new ResponseDto<>(loginResult, message), HttpStatus.OK);
    }

    @GetMapping("/token")
    public String getAccessToken(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return customUserDetails.getAccessToken();
    }

    @DeleteMapping("/")
    public ResponseEntity<ResponseDto<Void>> deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userAuthService.deleteUser(Long.valueOf(customUserDetails.getId()));

        return new ResponseEntity<>(new ResponseDto<>(null, MessagesSuccess.DELETE_USER_SUCCESS.getMessage()), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<ResponseDto<UserInfoResponseDto>> getUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        UserInfoResponseDto response = userService.getUserInfo(Long.valueOf(customUserDetails.getId()));

        return new ResponseEntity<>(new ResponseDto<>(response, MessagesSuccess.RETRIEVE_USER_INFO_SUCCESS.getMessage()), HttpStatus.OK);
    }
}
