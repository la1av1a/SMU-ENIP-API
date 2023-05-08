package com.smu.smuenip.domain.dto;

import lombok.Getter;

@Getter
public class RankDto {

    private String login_id;
    private int score;
    private Long rank;

    public RankDto(String login_id, int score, Long rank) {
        this.login_id = login_id;
        this.score = score;
        this.rank = rank;
    }
}
