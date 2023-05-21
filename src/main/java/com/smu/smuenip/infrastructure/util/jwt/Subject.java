package com.smu.smuenip.infrastructure.util.jwt;

import com.smu.smuenip.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Subject {

    private final Long id;
    private final String userId;
    private final String email;
    private final Role role;

    @Builder
    public Subject(Long id, String userId, String email, Role role) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public static Subject atk(Long id, String userId, String email,
                              Role role) {
        return Subject.builder()
                .id(id)
                .userId(userId)
                .email(email)
                .role(role)
                .build();
    }
}
