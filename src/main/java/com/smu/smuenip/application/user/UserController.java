package com.smu.smuenip.application.user;

import com.smu.smuenip.application.login.dto.ResponseDto;
import com.smu.smuenip.application.login.dto.UserInfoResponseDto;
import com.smu.smuenip.domain.user.serivce.UserAuthService;
import com.smu.smuenip.domain.user.serivce.UserService;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesSuccess;
import com.smu.smuenip.infrastructure.config.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserAuthService userAuthService;
    private final UserService userService;

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
