package com.smu.smuenip.domain.dto;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class RankDto implements Serializable {

    private String nickName;
    private int score;

    public RankDto(String nickName, int score) {
        this.nickName = nickName;
        this.score = score;
    }
}
