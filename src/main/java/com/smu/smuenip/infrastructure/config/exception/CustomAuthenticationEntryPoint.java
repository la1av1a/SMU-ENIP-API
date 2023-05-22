package com.smu.smuenip.infrastructure.config.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.smuenip.application.login.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Configuration
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        if (authException instanceof BadCredentialsException) {
            // 잘못된 자격 증명 관련 에러 처리
            responseError(response, "Bad Credentials");
        } else if (authException instanceof DisabledException) {
            // 사용자 계정이 비활성화된 관련 에러 처리
            responseError(response, "Account Disabled");
        } else if (authException instanceof LockedException) {
            // 사용자 계정이 잠긴 관련 에러 처리
            responseError(response, "Account Locked");
        } else {
            // 기타 인증 관련 에러 처리
            responseError(response, authException.getMessage());
        }
    }

    private void responseError(HttpServletResponse response, String message)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ResponseEntity<ResponseDto<Void>> responseDto = new ResponseEntity<>(new ResponseDto<>(null, message), UNAUTHORIZED);

        objectMapper.writeValue(response.getWriter(), responseDto);
    }
}