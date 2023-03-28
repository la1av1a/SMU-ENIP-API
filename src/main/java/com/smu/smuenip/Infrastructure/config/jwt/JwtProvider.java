package com.smu.smuenip.Infrastructure.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.smuenip.enums.TokenType;
import io.jsonwebtoken.security.Keys;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Getter
public class JwtProvider {

    private final ObjectMapper objectMapper;

    @Value("${spring.jwt.key}")
    private String key;

    @Value("${spring.jwt.live.accessToken}")
    private Long atkTokenLive;

    @Value("${spring.jwt.live.refreshToken}")
    private Long rtkTokenLive;
    private SecretKey secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }


    public Long getTokenLive(TokenType tokenType) {
        return tokenType == TokenType.ATK ? atkTokenLive : rtkTokenLive;
    }

}
