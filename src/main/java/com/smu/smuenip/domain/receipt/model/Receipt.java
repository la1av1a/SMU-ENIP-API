package com.smu.smuenip.domain.receipt.model;

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

@NoArgsConstructor
@Getter
@Entity
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receipt_id;

    @JoinColumn(name = "users_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @Column
    private String receipt_url;

    @CreatedDate
    private LocalDate uploadedDate = LocalDate.now();

    @Builder
    public Receipt(User user, String receipt_url) {
        this.user = user;
        this.receipt_url = receipt_url;
    }
}
