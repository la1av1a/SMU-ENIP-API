package com.smu.smuenip.domain.purchasedItem.model;

import com.smu.smuenip.domain.category.entity.Category;
import com.smu.smuenip.domain.receipt.model.Receipt;
import com.smu.smuenip.domain.recycledImage.entity.RecycledImage;
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
public class PurchasedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchasedItemId;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "receipt_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Receipt receipt;

    @Column
    private String itemName;

    @Column
    private String imageUrl;

    @JoinColumn(name = "category")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Column
    private int itemCount;

    @Column
    private int itemPrice;

    @Column
    private int trashAmount;

    @OneToOne(mappedBy = "purchasedItem", fetch = FetchType.LAZY)
    private RecycledImage recycledImage;

    @Column
    private LocalDate purchasedDate;

    @Builder
    public PurchasedItem(User user, Receipt receipt, String itemName, String imageUrl,
        int itemCount, int itemPrice, int trashAmount, Category category, LocalDate purchasedDate) {
        this.user = user;
        this.receipt = receipt;
        this.itemName = itemName;
        this.imageUrl = imageUrl;
        this.itemCount = itemCount;
        this.itemPrice = itemPrice;
        this.trashAmount = trashAmount;
        this.category = category;
        this.purchasedDate = purchasedDate;
    }
}
