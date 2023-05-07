package com.smu.smuenip.domain.dto;

import lombok.Getter;

@Getter
public class RankDto {

  private Long id;
  private String login_id;
  private Long rank;

  public RankDto(Long id, String login_id, Long rank) {
    this.id = id;
    this.login_id = login_id;
    this.rank = rank;
  }
}
