package com.smu.smuenip.domain.user.model;

import com.smu.smuenip.enums.Role;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

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
    private LocalDate createdDate = LocalDate.now();

    @Builder
    public User(String loginId, String email, int score,
        Role role) {
        this.loginId = loginId;
        this.email = email;
        this.score = score;
        this.role = role;
    }
}
