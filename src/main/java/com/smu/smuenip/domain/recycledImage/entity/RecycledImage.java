package com.smu.smuenip.domain.recycledImage.entity;

import com.smu.smuenip.domain.image.Receipt;
import com.smu.smuenip.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
public class RecycledImage {

    @Id
    @Column(name = "recycled_image_id")
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Receipt receipt;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private String recycledImageUrl;

    @Column
    private LocalDate uploaded_date;

    @Column
    private boolean isChecked;

    @Column
    private boolean isApproved;

    @JoinColumn
    @OneToOne
    private User approvedBy;

    @JoinColumn
    private LocalDate approvedDate;

    @Builder
    public RecycledImage(Receipt receipt, User user, String recycledImageUrl, LocalDate uploaded_date, boolean isChecked, boolean isApproved, User approvedBy, LocalDate approvedDate) {
        this.receipt = receipt;
        this.user = user;
        this.recycledImageUrl = recycledImageUrl;
        this.uploaded_date = uploaded_date;
        this.isChecked = isChecked;
        this.isApproved = isApproved;
        this.approvedBy = approvedBy;
        this.approvedDate = approvedDate;
    }
}
