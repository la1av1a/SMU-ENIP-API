package com.smu.smuenip.domain.auth.service;

import com.smu.smuenip.Infrastructure.config.exception.BadRequestException;
import com.smu.smuenip.Infrastructure.config.exception.UnauthorizedException;
import com.smu.smuenip.Infrastructure.config.jwt.JwtProvider;
import com.smu.smuenip.Infrastructure.config.jwt.Subject;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.TokenType;
import com.smu.smuenip.enums.meesagesDetail.MessagesFail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final UserRepository userRepository;
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

        User principal = findUserById(subject.getId());

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

    private User findUserById(Long id) {
        return userRepository.findUserById(id)
            .orElseThrow(() -> new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }

//    private Collection<GrantedAuthority> createAuthorities(List<String> authoritiesList) {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        for (String authority : authoritiesList) {
//            authorities.add(new SimpleGrantedAuthority(authority));
//        }
//
//        return authorities;
//    }

    private void checkAuthorities(Claims claims) {
        if (claims.get("authorities") == null) {
            throw new UnauthorizedException(MessagesFail.UNAUTHORIZED.getMessage());
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(jwtProvider.getSecretKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            log.warn(e.toString());
            return false;
        }
    }
}
