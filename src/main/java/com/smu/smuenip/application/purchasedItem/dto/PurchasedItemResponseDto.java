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
    private String categoryImage;
    private String receiptImage;
    private String date;
    private String name;
    @JsonProperty("receiptList")
    private Long receiptId;
    private String trashAmount;
    private String expenditureCost;
    private boolean isRecycled;
    private String category;

    @Builder
    public PurchasedItemResponseDto(Long purchasedItemId, String categoryImage,
        LocalDate date,
        Long receiptId, int trashAmount, String expenditureCost, boolean isRecycled,
        String category, String receiptImage, String name) {
        this.purchasedItemId = purchasedItemId;
        this.categoryImage = categoryImage;
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.receiptId = receiptId;
        this.trashAmount = trashAmount + "g";
        this.expenditureCost = expenditureCost;
        this.name = name;
        this.isRecycled = isRecycled;
        this.category = category;
        this.receiptImage = receiptImage;
    }
}
