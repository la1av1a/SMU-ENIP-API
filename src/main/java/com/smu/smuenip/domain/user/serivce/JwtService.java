package com.smu.smuenip.domain.user.serivce;

import com.smu.smuenip.Infrastructure.config.exception.BadRequestException;
import com.smu.smuenip.Infrastructure.config.exception.UnAuthorizedException;
import com.smu.smuenip.Infrastructure.config.jwt.JwtProvider;
import com.smu.smuenip.Infrastructure.config.jwt.Subject;
import com.smu.smuenip.Infrastructure.config.redis.TokenInfo;
import com.smu.smuenip.Infrastructure.config.redis.TokenInfoRepository;
import com.smu.smuenip.enums.Role;
import com.smu.smuenip.enums.meesagesDetail.MessagesFail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
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

    private final TokenInfoRepository tokenInfoRepository;
    private final JwtProvider jwtProvider;

    public String createToken(Subject subject) {
        Date now = new Date();
        long tokenValidity = jwtProvider.getTokenLive();

        return Jwts.builder()
            .setIssuedAt(now)
            .setSubject(subject.getId().toString())
            .claim("userId", subject.getUserId())
            .claim("email", subject.getEmail())
            .claim("role", subject.getRole())
            .setIssuedAt(new Date())
            .setExpiration(new Date(now.getTime() + tokenValidity))
            .signWith(jwtProvider.getSecretKey())
            .compact();
    }

    public Authentication getAuthenticationFromToken(String token) {
        Claims claims = extractClaimsFromToken(token);

        Subject subject = extractSubjectFromClaims(claims);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(subject.getRole().toString()));

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

    private Subject extractSubjectFromClaims(Claims claims) {
        Long id = Long.valueOf(claims.getSubject());
        String userId = claims.get("userId", String.class);
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);
        log.info("role : {}", role);
        checkAuthorities(claims);

        return Subject.builder()
            .id(id)
            .userId(userId)
            .email(email)
            .role(Role.valueOf(role))
            .build();
    }

    private TokenInfo findTokenInfoById(Long id) {
        return tokenInfoRepository.findById(Long.toString(id))
            .orElseThrow(() -> new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }

    private void checkAuthorities(Claims claims) throws UnAuthorizedException {
        if (claims.get("role") == null) {
            throw new UnAuthorizedException("클레임이 존재하지 않습니다 클레임 : role");
        }
    }

    public boolean validateToken(String token) throws UnAuthorizedException {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(jwtProvider.getSecretKey())
                .build()
                .parseClaimsJws(token);
            Optional<TokenInfo> tokenInfo = tokenInfoRepository.findById(
                claims.getBody().getSubject());
            if (!tokenInfo.isPresent()) {
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

    public Long getTokenLive() {
        return jwtProvider.getTokenLive();
    }
}
