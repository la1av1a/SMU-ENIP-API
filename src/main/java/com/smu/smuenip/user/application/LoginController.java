package com.smu.smuenip.user.application;

import com.smu.smuenip.infra.config.security.filters.CustomUserDetails;
import com.smu.smuenip.user.application.dto.UserLoginRequestDto;
import com.smu.smuenip.user.application.dto.UserRequestDto;
import com.smu.smuenip.user.application.enums.Messages;
import com.smu.smuenip.user.application.enums.meesagesDetail.MessagesFail;
import com.smu.smuenip.user.application.enums.meesagesDetail.MessagesSuccess;
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
public class LoginController implements LoginControllerSwagger {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<Messages> signUp(@RequestBody UserRequestDto requestDto) {
        boolean isSuccess = userService.createUser(requestDto);

        if (isSuccess) {
            return new ResponseEntity<>(MessagesSuccess.SIGNUP_SUCCESS, HttpStatus.OK);
        }

        return new ResponseEntity<>(MessagesFail.USER_EXISTS, HttpStatus.CONFLICT);
    }


    @PostMapping("/login")
    public String login(@RequestBody UserLoginRequestDto requestDto) {
        return userService.login(requestDto);
    }


    @GetMapping("/loginTest")
    public String loginTest(@AuthenticationPrincipal CustomUserDetails user) {
        return user.getUserId();
    }

}
