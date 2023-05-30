package com.smu.smuenip.application.admin;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class ApproveRequestDto {

    @NotEmpty
    private Long recycledImageId;
    @NotEmpty
    private boolean approve;
}
