package com.smu.smuenip.application.purchasedItem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PurchasedItemResponseDto {

    @JsonProperty("id")
    private Long purchasedItemId;
    private String purchasedItemExampleImage;
    private String receiptImage;
    private String date;
    @JsonProperty("receiptList")
    private Long receiptId;
    private String trashAmount;
    private String expenditureCost;
    private boolean isRecycled;
    private String category;

    @Builder
    public PurchasedItemResponseDto(Long purchasedItemId, String purchasedItemExampleImage,
        LocalDate date,
        Long receiptId, int trashAmount, String expenditureCost, boolean isRecycled,
        String category, String receiptImage) {
        this.purchasedItemId = purchasedItemId;
        this.purchasedItemExampleImage = purchasedItemExampleImage;
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.receiptId = receiptId;
        this.trashAmount = trashAmount + "g";
        this.expenditureCost = expenditureCost;
        this.isRecycled = isRecycled;
        this.category = category;
        this.receiptImage = receiptImage;
    }
}
