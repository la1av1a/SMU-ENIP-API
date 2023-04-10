package com.smu.smuenip.domain.image;

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

@Getter
@Entity
@NoArgsConstructor
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Column
    private String imageUrl;

    @Column
    private String comment;

    @CreatedDate
    private LocalDate purchasedDate = LocalDate.now();

    @Builder
    public Receipt(String imageUrl, User user) {
        this.imageUrl = imageUrl;
        this.user = user;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
