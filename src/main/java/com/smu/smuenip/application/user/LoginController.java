package com.smu.smuenip.application.user;

import com.smu.smuenip.Infrastructure.config.security.filters.CustomUserDetails;
import com.smu.smuenip.application.user.dto.UserLoginRequestDto;
import com.smu.smuenip.application.user.dto.UserRequestDto;
import com.smu.smuenip.enums.Messages;
import com.smu.smuenip.enums.meesagesDetail.MessagesFail;
import com.smu.smuenip.enums.meesagesDetail.MessagesSuccess;
import com.smu.smuenip.domain.user.serivce.UserService;
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
