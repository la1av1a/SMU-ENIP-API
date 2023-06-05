package com.smu.smuenip.domain.recycledImage.entity;

import com.smu.smuenip.domain.purchasedItem.model.PurchasedItem;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "purchased_item_id")
    @OneToOne
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

    @ManyToOne
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
}
