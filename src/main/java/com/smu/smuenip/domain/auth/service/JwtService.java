package com.smu.smuenip.domain.auth.service;

import com.smu.smuenip.Infrastructure.config.exception.BadRequestException;
import com.smu.smuenip.Infrastructure.config.exception.UnAuthorizedException;
import com.smu.smuenip.Infrastructure.config.jwt.JwtProvider;
import com.smu.smuenip.Infrastructure.config.jwt.Subject;
import com.smu.smuenip.Infrastructure.config.redis.TokenInfo;
import com.smu.smuenip.Infrastructure.config.redis.TokenInfoRepository;
import com.smu.smuenip.enums.TokenType;
import com.smu.smuenip.enums.meesagesDetail.MessagesFail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final TokenInfoRepository tokenInfoRepository;
    private final JwtProvider jwtProvider;

    public String createToken(Subject subject) {
        Date now = new Date();
        long tokenValidity = jwtProvider.getTokenLive(subject.getType());

        return Jwts.builder()
            .setIssuedAt(now)
            .setSubject(subject.getId().toString())
            .claim("userId", subject.getUserId())
            .claim("email", subject.getEmail())
            .claim("type", subject.getType())
            .claim("authorities", subject.getAuthorities())
            .setExpiration(new Date(now.getTime() + tokenValidity))
            .signWith(jwtProvider.getSecretKey())
            .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = extractClaimsFromToken(token);

        Subject subject = extractUserInfoFromClaims(claims);
        Collection<GrantedAuthority> authorities = subject.getAuthorities();

        TokenInfo principal = findTokenInfoById(subject.getId());

        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    private Claims extractClaimsFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(jwtProvider.getSecretKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Subject extractUserInfoFromClaims(Claims claims) {
        Long id = Long.valueOf(claims.getSubject());
        String userId = claims.get("userId", String.class);
        String email = claims.get("email", String.class);
        TokenType type = TokenType.valueOf(claims.get("type", String.class));
        checkAuthorities(claims);

        Collection<GrantedAuthority> authorities = Arrays.stream(
                claims.get("authorities").toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        return Subject.builder()
            .id(id)
            .userId(userId)
            .email(email)
            .type(type)
            .authorities(authorities)
            .build();
    }

    private TokenInfo findTokenInfoById(Long id) {
        return tokenInfoRepository.findById(Long.toString(id))
            .orElseThrow(() -> new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }

    private void checkAuthorities(Claims claims) {
        if (claims.get("authorities") == null) {
            throw new UnAuthorizedException("클레임이 존재하지 않습니다 클레임 : authorities");
        }
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = (Claims) Jwts.parserBuilder()
                .setSigningKey(jwtProvider.getSecretKey())
                .build()
                .parseClaimsJws(token);

            Optional<TokenInfo> tokenInfo = tokenInfoRepository.findById(claims.getId());
            if (!tokenInfo.isPresent()) {
                throw new UnAuthorizedException("만료된 토큰입니다");
            } else if (tokenInfo.get().getCreatedAt() != claims.getIssuedAt()) {
                throw new UnAuthorizedException("유효하지 않은 토큰입니다");
            }

        } catch (SignatureException | MalformedJwtException e) {
            throw new UnAuthorizedException("유효하지 않은 토큰입니다");
        } catch (ExpiredJwtException e) {
            throw new UnAuthorizedException("만료된 토큰입니다");
        } catch (JwtException e) {
            throw new UnAuthorizedException("JWT 검증 중 예기치 않은 오류가 발생했습니다");
        }
        return true;
    }

    public Long getTokenLive(TokenType tokenType) {
        return jwtProvider.getTokenLive(tokenType);
    }
}
