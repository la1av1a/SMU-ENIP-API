package com.smu.smuenip.Infrastructure.config.redis;

import java.time.Instant;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("token")
public class TokenInfo {

    @Id
    private String id;
    private String userId;
    private String email;
    private String accessToken;
    private String refreshToken;
    private Instant accessTokenExpiration;
    private Instant refreshTokenExpiration;

    @Builder
    public TokenInfo(String id, String userId, String email, String accessToken,
        String refreshToken,
        Instant accessTokenExpiration, Instant refreshTokenExpiration) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }
}
