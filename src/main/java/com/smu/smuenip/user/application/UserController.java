package com.smu.smuenip.user.application;

import com.smu.smuenip.infra.config.security.filters.CustomUserDetails;
import com.smu.smuenip.user.application.dto.UserLoginRequestDto;
import com.smu.smuenip.user.application.dto.UserRequestDto;
import com.smu.smuenip.user.application.enums.Messages;
import com.smu.smuenip.user.application.enums.MessagesFail;
import com.smu.smuenip.user.application.enums.MessagesSuccess;
import com.smu.smuenip.user.domain.serivce.UserService;
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
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    private ResponseEntity<Messages> signUp(@RequestBody UserRequestDto requestDto) {
        boolean isSuccess = userService.createUser(requestDto);

        if (isSuccess) {
            return new ResponseEntity<>(MessagesSuccess.SIGNUP_SUCCESS, HttpStatus.OK);
        }

        return new ResponseEntity<>(MessagesFail.USER_EXISTS, HttpStatus.CONFLICT);
    }

    @PostMapping("/login")
    private String login(@RequestBody UserLoginRequestDto requestDto) {
        return userService.login(requestDto);
    }

    @GetMapping("/loginTest")
    private String loginTest(@AuthenticationPrincipal CustomUserDetails user) {
        return user.getUserId();
    }

}
