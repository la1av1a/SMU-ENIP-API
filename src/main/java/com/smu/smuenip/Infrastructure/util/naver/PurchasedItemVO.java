package com.smu.smuenip.Infrastructure.util.naver;

import lombok.Getter;

@Getter
public class PurchasedItemVO {

    private final String name;
    private final String count;
    private final String price;

    public PurchasedItemVO(String name, String count, String price) {
        this.name = name;
        this.count = count;
        this.price = price;
    }

}
