package com.smu.smuenip.domain.dto;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class RankDto implements Serializable {

    private final String nickName;
    private final int score;
    private final int weight;

    public RankDto(String nickName, int score, int weight) {
        this.nickName = nickName;
        this.score = score;
        this.weight = weight;
    }
}
