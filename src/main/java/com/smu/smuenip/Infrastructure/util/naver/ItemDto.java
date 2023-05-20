package com.smu.smuenip.Infrastructure.util.naver;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class ItemDto implements Serializable {

    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<Item> items = new ArrayList<>();
}
