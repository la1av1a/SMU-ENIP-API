package com.smu.smuenip.application.purchasedItem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class PurchasedItemResponseDto {

    @JsonProperty("id")
    private Long purchasedItemId;
    private String date;
    @JsonProperty("receiptList")
    private Long receiptId;
    private int trashAmount = 0;
    private String expenditureCost;

    @Builder
    public PurchasedItemResponseDto(Long purchasedItemId, LocalDate date, Long receiptId, int trashAmount, String expenditureCost) {
        this.purchasedItemId = purchasedItemId;
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.receiptId = receiptId;
        this.trashAmount = trashAmount;
        this.expenditureCost = expenditureCost;
    }
}