package com.smu.smuenip.domain.receipt;

import com.smu.smuenip.domain.user.model.User;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Entity
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    private String imageUrl;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private boolean isRecycled;

    @CreatedDate
    private LocalDate createdDate = LocalDate.now();

    @LastModifiedDate
    private LocalDate modifiedDate = LocalDate.now();

    @Builder
    public Image(String imageUrl, User user, boolean isRecycled) {
        this.imageUrl = imageUrl;
        this.user = user;
        this.isRecycled = isRecycled;
    }
}