package com.smu.smuenip.Infrastructure.config.redis;

import com.smu.smuenip.domain.user.model.Role;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@RedisHash("token")
public class TokenInfo implements UserDetails {

    @Id
    private String id;
    private String loginId;
    private String email;
    private String accessToken;
    private String refreshToken;

    private Collection<Role> roles = new ArrayList<>();
    private Long accessTokenExpiration;
    private Long refreshTokenExpiration;

    @Builder
    public TokenInfo(String id, String loginId, String email, String accessToken,
        String refreshToken,
        Long accessTokenExpiration, Long refreshTokenExpiration,
        Collection<Role> roles
    ) {
        this.id = id;
        this.loginId = loginId;
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.roles = roles;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    public String getLoginId() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
