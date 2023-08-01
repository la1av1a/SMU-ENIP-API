package com.smu.smuenip.domain.recycledImage.entity;

import com.smu.smuenip.domain.purchasedItem.model.PurchasedItem;
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
import javax.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class RecycledImage {

    @Id
    @Column(name = "recycled_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "purchased_item_id")
    @OneToOne(fetch = FetchType.LAZY)
    private PurchasedItem purchasedItem;

    @Column
    private String recycledImageUrl;

    @Column(name = "original_image_url")
    private String originalImageUrl;

    @Column
    private LocalDate uploadDate;

    @Column
    private boolean isChecked;

    @Column
    private boolean isApproved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by", referencedColumnName = "user_id")
    private User approvedBy;

    @Builder
    public RecycledImage(Long id, PurchasedItem purchasedItem, String recycledImageUrl,
        String originalImageUrl,
        LocalDate uploadDate, boolean isChecked, boolean isApproved, User approvedBy) {
        this.id = id;
        this.purchasedItem = purchasedItem;
        this.recycledImageUrl = recycledImageUrl;
        this.originalImageUrl = originalImageUrl;
        this.uploadDate = uploadDate;
        this.isChecked = isChecked;
        this.isApproved = isApproved;
        this.approvedBy = approvedBy;
    }

    public void setApproved(boolean approved) {

        User user = this.purchasedItem.getUser();

        isChecked = true;
        if (approved) {
            user.incrementScore();

            isApproved = true;
            return;
        }

        user.decrementScore();
        isApproved = false;
        setApprovedByNull();
    }

    public void setApprovedBy(User user) {
        this.approvedBy = user;
    }

    public void setApprovedByNull() {
        this.approvedBy = null;
    }
}
