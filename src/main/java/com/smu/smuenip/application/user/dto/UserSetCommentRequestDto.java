package com.smu.smuenip.application.user.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class UserSetCommentRequestDto {

    @NotEmpty
    private Long receiptId;
    @NotEmpty
    private String comment;

}
