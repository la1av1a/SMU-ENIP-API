package com.smu.smuenip.Infrastructure.config.jwt;

import java.util.Collection;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class Subject {

    private final Long id;
    private final String userId;
    private final String email;
    private final Collection<GrantedAuthority> authorities;

    @Builder
    public Subject(Long id, String userId, String email, Collection<GrantedAuthority> authorities) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.authorities = authorities;
    }

    public static Subject atk(Long id, String userId, String email,
        Collection<GrantedAuthority> authorities) {
        return Subject.builder()
            .id(id)
            .userId(userId)
            .email(email)
            .authorities(authorities)
            .build();
    }
}
