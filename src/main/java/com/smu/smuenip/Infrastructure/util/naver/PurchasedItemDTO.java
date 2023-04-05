package com.smu.smuenip.Infrastructure.util.naver;

import lombok.Getter;

@Getter
public class PurchasedItemDTO {

    private final String name;
    private final String count;
    private final String price;

    public PurchasedItemDTO(String name, String count, String price) {
        this.name = name;
        this.count = count;
        this.price = price;
    }

}
