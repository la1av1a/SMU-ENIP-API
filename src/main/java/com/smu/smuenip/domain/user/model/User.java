package com.smu.smuenip.domain.user.model;

import com.smu.smuenip.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Entity
@Table(name = "user")
@NoArgsConstructor
public class User implements Serializable {

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

    @Column
    private int weight;

    @CreatedDate
    private LocalDate createdDate;

    @Builder
    public User(String email, String nickName, Role role, int score, int weight) {
        this.email = email;
        this.nickName = nickName;
        this.role = role;
        this.score = score;
        this.weight = weight;
        this.createdDate = LocalDate.now();
    }

    public void incrementScore() {
        this.score += 1;
    }

    public void decrementScore() {
        this.score -= 1;
    }
}