package com.smu.smuenip.domain.receipt.model;

import com.smu.smuenip.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_id")
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Column
    private String imageUrl;
    @Column(name = "original_image_url")
    private String originalImageUrl;

    @Column
    private String comment;

    @Column
    private boolean isRecycled;

    @CreatedDate
    private LocalDate purchasedDate;

    @Builder
    public Receipt(String imageUrl, String originalImageUrl, User user, LocalDate purchasedDate) {
        this.imageUrl = imageUrl;
        this.originalImageUrl = originalImageUrl;
        this.user = user;
        this.isRecycled = false;
        this.purchasedDate = purchasedDate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
