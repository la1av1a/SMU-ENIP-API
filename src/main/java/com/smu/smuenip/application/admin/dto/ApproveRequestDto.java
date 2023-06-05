package com.smu.smuenip.application.admin.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class ApproveRequestDto {

    @NotEmpty
    private Long recycledImageId;
    @NotEmpty
    private boolean approve;
}
