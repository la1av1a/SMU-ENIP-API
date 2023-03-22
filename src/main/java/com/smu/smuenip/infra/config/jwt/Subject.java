package com.smu.smuenip.infra.config.jwt;

import lombok.Getter;

@Getter
public class Subject {

    private final Long id;
    private final String userId;

    private final String email;

    private final String type;

    private Subject(Long id, String userId, String email, String type) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.type = type;
    }

    public static Subject atk(Long id, String userId, String email) {
        return new Subject(id, userId, email, "ATK");
    }

    public static Subject rtk(Long id, String userId, String email) {
        return new Subject(id, userId, email, "RTK");
    }
}
