package com.smu.smuenip.Infrastructure.util.naver;


import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class ItemDTO {

    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<Item> items = new ArrayList<>();

    public ItemDTO(String lastBuildDate, int total, int start, int display, List<Item> items) {
        this.lastBuildDate = lastBuildDate;
        this.total = total;
        this.start = start;
        this.display = display;
        this.items = items;
    }
}
