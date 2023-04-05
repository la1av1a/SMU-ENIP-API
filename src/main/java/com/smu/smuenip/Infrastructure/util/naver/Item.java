package com.smu.smuenip.Infrastructure.util.naver;

import lombok.Getter;


@Getter
public class Item {

    private String title;
    private String link;
    private String image;
    private int lprice;
    private int hprice;
    private String mallName;
    private String productId;
    private int productType;
    private String brand;
    private String maker;
    private String category1;
    private String category2;
    private String category3;
    private String category4;

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    public void setCategory4(String category4) {
        this.category4 = category4;
    }
}
