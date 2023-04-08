package com.smu.smuenip.Infrastructure.config.redis;

import com.smu.smuenip.enums.Role;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@RedisHash(value = "token")
public class TokenInfo {

    @Id
    private String id;
    private String loginId;
    private String email;
    private String accessToken;

    private Role role;

    private Collection<GrantedAuthority> authorities = new ArrayList<>();
    private Long accessTokenExpiration;
    private Date createdAt;

    @Builder
    public TokenInfo(String id, String loginId, String email, String accessToken,
        Long accessTokenExpiration,
        Role role,
        Date createdAt
    ) {
        this.id = id;
        this.loginId = loginId;
        this.email = email;
        this.accessToken = accessToken;
        this.accessTokenExpiration = accessTokenExpiration;
        this.role = role;
        this.authorities.add(new SimpleGrantedAuthority(role.toString()));
        this.createdAt = createdAt;
    }
}
