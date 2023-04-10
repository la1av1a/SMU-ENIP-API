package com.smu.smuenip.application.purchasedItem.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PurchasedItemResponseDto {

    private Long itemId;
    private Long userId;
    private String imageUrl;
    private String itemName;
    private int itemCount;
    private int itemPrice;
    private String recycleExampleImageUrl;
    private boolean isRecycled;

    @Builder
    public PurchasedItemResponseDto(Long itemId, Long userId, String imageUrl, String itemName,
        int itemCount, int itemPrice, String recycleExampleImageUrl, boolean isRecycled) {
        this.itemId = itemId;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.itemName = itemName;
        this.itemCount = itemCount;
        this.itemPrice = itemPrice;
        this.recycleExampleImageUrl = recycleExampleImageUrl;
        this.isRecycled = isRecycled;
    }
}
