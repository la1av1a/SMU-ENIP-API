package com.smu.smuenip.infra.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.smuenip.infra.config.security.filters.CustomUserDetails;
import com.smu.smuenip.user.domain.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final ObjectMapper objectMapper;

    @Value("${spring.jwt.key}")
    private String key;

    @Value("${spring.jwt.live.accessToken}")
    private Long tokenLive;
    private SecretKey secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String createToken(Subject subject) {
        String subjectStr = "";
        List<String> roles = new ArrayList<>();
        roles.add(Role.ROLE_USER.name());
        try {
            subjectStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(subject);
        } catch (JsonProcessingException jpe) {
            jpe.printStackTrace();
            throw new RuntimeException();
        }

        Claims claims = Jwts.claims()
            .setSubject(subjectStr);
        Date date = new Date();

        return Jwts.builder()
            .setIssuedAt(date)
            .claim("sub", subject.getId())
            .claim("userId", subject.getUserId())
            .claim("email", subject.getEmail())
            .claim("type", subject.getType())
            .claim("roles", roles)
            .setExpiration(new Date(date.getTime() + tokenLive))
            .signWith(secretKey)
            .compact();
    }

    // 인증 정보 조회

    // 토큰에서 회원 정보 추출

    // 토큰 유효성, 만료일자 확인
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            //TODO 에러처리
            log.warn(e.toString());
            return false;
        }
    }

    // Request의 Header에서 token 값 가져오기 (Authorization)
    public Authentication getAuthentication(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token);
        Claims claims = claimsJws.getBody();

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(claims.get("roles").toString()));

        // 토큰에서 사용자 정보 추출하여 Users 객체 생성
        Long id = Long.valueOf(claims.getSubject());
        String userId = claims.get("userId").toString();
        String userEmail = claims.get("email").toString();
        CustomUserDetails principal = CustomUserDetails.builder()
            .id(id)
            .userId(userId)
            .email(userEmail)
            .authorities(authorities)
            .build();

        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

}
