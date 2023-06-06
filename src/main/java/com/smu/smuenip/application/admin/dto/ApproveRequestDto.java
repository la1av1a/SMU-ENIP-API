package com.smu.smuenip.application.admin.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ApproveRequestDto {

    @NotNull
    private Long recycledImageId;
    
    @NotNull
    private boolean approve;
}
