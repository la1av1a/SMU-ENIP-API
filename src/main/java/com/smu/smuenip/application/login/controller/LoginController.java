package com.smu.smuenip.application.login.controller;

import com.smu.smuenip.application.login.dto.LoginRequestDto;
import com.smu.smuenip.application.login.dto.LoginResult;
import com.smu.smuenip.application.login.dto.ResponseDto;
import com.smu.smuenip.application.login.dto.UserRequestDto;
import com.smu.smuenip.domain.user.serivce.UserAuthService;
import com.smu.smuenip.enums.Role;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesSuccess;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RequestMapping("/user")
@RequiredArgsConstructor
@RestController
public class LoginController implements LoginControllerSwagger {

    private final UserAuthService userAuthService;

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
}
