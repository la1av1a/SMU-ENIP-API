package com.smu.smuenip.domain.recycledImage;

import com.smu.smuenip.domain.PurchasedItem.model.PurchasedItem;
import com.smu.smuenip.domain.user.model.User;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class RecycledImage {

    @Id
    @Column(name = "recycled_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "purchased_item_id")
    @OneToOne
    private PurchasedItem purchasedItem;

    @Column
    private String recycledImageUrl;

    @Column
    private LocalDate uploadDate;

    @Column
    private boolean isChecked;

    @Column
    private boolean isApproved;

    @ManyToOne
    @JoinColumn(name = "approved_by", referencedColumnName = "user_id")
    private User approvedBy;

    @Builder
    public RecycledImage(Long id, PurchasedItem purchasedItem, String recycledImageUrl,
        LocalDate uploadDate, boolean isChecked, boolean isApproved, User approvedBy) {
        this.id = id;
        this.purchasedItem = purchasedItem;
        this.recycledImageUrl = recycledImageUrl;
        this.uploadDate = uploadDate;
        this.isChecked = isChecked;
        this.isApproved = isApproved;
        this.approvedBy = approvedBy;
    }
}