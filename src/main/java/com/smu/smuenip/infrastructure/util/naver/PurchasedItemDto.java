package com.smu.smuenip.infrastructure.util.naver;

import lombok.Getter;

@Getter
public class PurchasedItemDto {

    private final String name;
    private final String count;
    private final String price;

    public PurchasedItemDto(String name, String count, String price) {
        this.name = name;
        this.count = count;
        this.price = price;
    }

}
