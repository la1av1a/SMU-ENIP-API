package com.smu.smuenip.domain.user.model;

import com.smu.smuenip.enums.Provider;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Table(name = "users_auth")
@Entity
@NoArgsConstructor
public class UserAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private Provider provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "password")
    private String password;


    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date modifiedDate;

    @Builder
    public UserAuth(User user, Provider provider, String providerId, String password) {
        this.user = user;
        this.provider = provider;
        this.providerId = providerId;
        this.password = password;
    }
}
