package com.smu.smuenip;

import com.smu.smuenip.Infrastructure.config.jwt.Subject;
import com.smu.smuenip.enums.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;

public class JwtTokenUtil {

    static final long EXPRIATION_TIME = 3200;
    static final String SECRET = "dlrjtdlwkqkekdlrjtdlwkqkekdlrjtdlwkqkekdlrjtdlwkqkekdlrjtdlwkqkekdlrjtdlwkqkek";

    static SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public static String createToken() {
        Subject subject = Subject.builder()
            .email("test1234@gmail.com")
            .id(1L)
            .userId("test1234")
            .role(Role.ROLE_USER)
            .build();

        Date now = new Date();
        long tokenValidity = EXPRIATION_TIME;

        return Jwts.builder()
            .setIssuedAt(now)
            .setSubject(subject.getId().toString())
            .claim("userId", subject.getUserId())
            .claim("email", subject.getEmail())
            .claim("role", subject.getRole())
            .setIssuedAt(new Date())
            .setExpiration(new Date(now.getTime() + tokenValidity))
            .signWith(key)
            .compact();
    }
}
