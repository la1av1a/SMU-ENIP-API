package com.smu.smuenip.Infrastructure.config.filters.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.smuenip.Infrastructure.config.exception.UnAuthorizedException;
import com.smu.smuenip.application.auth.dto.ResponseDto;
import com.smu.smuenip.domain.user.serivce.JwtService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && jwtService.validateToken(jwt)) {
                Authentication authentication = jwtService.getAuthenticationFromToken(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ResponseDto responseDto = new ResponseDto(false, ex.getMessage());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writer().writeValue(response.getWriter(), responseDto);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) throws UnAuthorizedException {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
