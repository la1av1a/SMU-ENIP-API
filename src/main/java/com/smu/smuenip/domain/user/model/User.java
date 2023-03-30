package com.smu.smuenip.domain.user.model;

import com.smu.smuenip.enums.Role;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long id;

    @Column
    private String loginId;


    @OneToMany(mappedBy = "user")
    private List<UserAuth> userAuths;

    @Column
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column
    private int score;


    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date modifiedDate;

    @Builder
    public User(String loginId, List<UserAuth> userAuths, String email, int score,
        Role role) {
        this.loginId = loginId;
        this.userAuths = userAuths;
        this.email = email;
        this.score = score;
        this.role = role;
    }
}
