package com.smu.smuenip.domain.PurchasedItem.model;

import com.smu.smuenip.domain.Category.model.Category;
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
public class PurchasedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchasedItemId;

    @JoinColumn(name = "user_id")
    @OneToOne
    private User user;

    @JoinColumn(name = "receipt_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Receipt receipt;

    @Column
    private String itemName;

    @Column
    private String imageUrl;

    @Column
    private int itemCount;

    @Column
    private int itemPrice;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Column
    private boolean isRecycled;

    @Column
    private LocalDate uploadedDate;

    @Builder
    public PurchasedItem(User user, Receipt receipt, String itemName, String imageUrl,
                         int itemCount, int itemPrice,
                         Category category, LocalDate uploadedDate) {
        this.user = user;
        this.receipt = receipt;
        this.itemName = itemName;
        this.imageUrl = imageUrl;
        this.itemCount = itemCount;
        this.itemPrice = itemPrice;
        this.category = category;
        this.uploadedDate = uploadedDate;
    }
}
