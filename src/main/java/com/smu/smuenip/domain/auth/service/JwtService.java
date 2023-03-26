package com.smu.smuenip.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.smuenip.Infrastructure.config.filters.CustomUserDetails;
import com.smu.smuenip.Infrastructure.config.jwt.JwtProvider;
import com.smu.smuenip.Infrastructure.config.jwt.Subject;
import com.smu.smuenip.domain.user.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

// 인증 정보 조회

// 토큰 유효성, 만료일자 확인

// Request의 Header에서 token 값 가져오기 (Authorization)
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;

    public String createToken(Subject subject,Authentication authentication) {
        String subjectStr = "";

        try {
            subjectStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(subject);
        } catch (JsonProcessingException jpe) {
            jpe.printStackTrace();
            throw new RuntimeException();
        }

        Date date = new Date();
        Long tokenLive = jwtProvider.getTokenLive(subject.getType());

        return Jwts.builder()
            .setIssuedAt(date)
            .claim("sub", subject.getId())
            .claim("userId", subject.getUserId())
            .claim("email", subject.getEmail())
            .claim("type", subject.getType())
            .claim("roles", roles)
            .setExpiration(new Date(date.getTime() + tokenLive))
            .signWith(jwtProvider.getSecretKey())
            .compact();
    }

    public Authentication getAuthentication(String token) {
        Jws<Claims> claimsJws = extractClaimsFromToken(token);
        Claims claims = claimsJws.getBody();
        Subject subject = extractUserInfoFromToken(claims);

        Collection<GrantedAuthority> authorities = createAuthorities(subject.getRole());
        CustomUserDetails principal = createCustomUserDetails(subject, authorities);

        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    private Jws<Claims> extractClaimsFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(jwtProvider.getSecretKey())
            .build()
            .parseClaimsJws(token);
    }

    // 토큰에서 회원 정보 추출
    private Subject extractUserInfoFromToken(Claims claims) {

        // 토큰에서 사용자 정보 추출하여 Users 객체 생성
        Long id = Long.valueOf(claims.getSubject());
        String userId = claims.get("userId").toString();
        String userEmail = claims.get("email").toString();
        Collection<Role> authorities = claims.get("roles").toString());

        return Subject.builder()
            .id(id)
            .userId(userId)
            .email(userEmail)
            .role(role)
            .build();
    }

    private Collection<GrantedAuthority> createAuthorities(Role role) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));

        return authorities;
    }

    private CustomUserDetails createCustomUserDetails(Subject subject,
        Collection<GrantedAuthority> authorities) {

        return CustomUserDetails.builder()
            .id(subject.getId())
            .userId(subject.getUserId())
            .email(subject.getEmail())
            .authorities(authorities)
            .build();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(jwtProvider.getSecretKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            //TODO 에러처리
            log.warn(e.toString());
            return false;
        }
    }
}
