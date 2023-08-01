package com.smu.smuenip.application.receipt.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class ReceiptSetCommentRequestDto {

    @NotEmpty
    private Long receiptId;
    @NotEmpty
    private String comment;

}
