package com.smu.smuenip;

import com.smu.smuenip.enums.Role;
import com.smu.smuenip.infrastructure.util.jwt.Subject;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtTokenUtil {

    static final long EXPRIATION_TIME = 3200;
    static final String SECRET = "dlrjtdlwkqkekdlrjtdlwkqkekdlrjtdlwkqkekdlrjtdlwkqkekdlrjtdlwkqkekdlrjtdlwkqkek";

    static SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public static String createToken(Long id, String email, String userId, Role role) {
        Subject subject = Subject.builder()
                .id(id)
                .email(email)
                .userId(userId)
                .role(role)
                .build();

        Date now = new Date();

        return Jwts.builder()
                .setIssuedAt(now)
                .setSubject(subject.getId().toString())
                .claim("userId", subject.getUserId())
                .claim("email", subject.getEmail())
                .claim("role", subject.getRole())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPRIATION_TIME))
                .signWith(key)
                .compact();
    }
}
