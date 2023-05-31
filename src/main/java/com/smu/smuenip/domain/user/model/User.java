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
    private String email;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private int score;

    @CreatedDate
    private LocalDate createdDate;

    @Builder
    public User(String email, String nickName, Role role, int score) {
        this.email = email;
        this.nickName = nickName;
        this.role = role;
        this.score = score;
        this.createdDate = LocalDate.now();
    }

    public void incrementScore() {
        this.score += 1;
    }

    public void decrementScore() {
        this.score -= 1;
    }
}