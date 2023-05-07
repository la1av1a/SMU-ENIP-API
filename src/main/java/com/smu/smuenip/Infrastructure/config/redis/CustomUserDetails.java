package com.smu.smuenip.Infrastructure.config.redis;

import com.smu.smuenip.enums.Role;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUserDetails implements UserDetails {

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
    public CustomUserDetails(String id, String loginId, String email, String accessToken,
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

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {

        return this.authorities;
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
