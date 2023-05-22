package com.smu.smuenip.domain.dto;

import lombok.Getter;

@Getter
public class RankDto {

    private String nickName;
    private int score;
    private Long rank;

    public RankDto(String nickName, int score, Long rank) {
        this.nickName = nickName;
        this.score = score;
        this.rank = rank;
    }
}
