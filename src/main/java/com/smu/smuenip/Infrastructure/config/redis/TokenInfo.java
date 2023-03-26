package com.smu.smuenip.Infrastructure.config.redis;

import com.smu.smuenip.domain.user.model.Role;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
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

    private Collection<Role> roles = new ArrayList<>();
    private Instant accessTokenExpiration;
    private Instant refreshTokenExpiration;

    @Builder
    public TokenInfo(String id, String userId, String email, String accessToken,
        String refreshToken,
        Instant accessTokenExpiration, Instant refreshTokenExpiration,
        Role role
    ) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        roles.add(role);
    }


}
