package com.smu.smuenip.domain.user.model;

import com.smu.smuenip.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@Table(name = "user")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column
    private String loginId;

    @Column
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private int score;

    @CreatedDate
    private LocalDate createdDate;

    @Builder
    public User(String loginId, String email, Role role, int score) {
        this.loginId = loginId;
        this.email = email;
        this.role = role;
        this.score = score;
        this.createdDate = LocalDate.now();
    }
}