package com.smu.smuenip.application.Receipt.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class ReceiptSetCommentRequestDto {

    @NotEmpty
    private Long receiptId;
    @NotEmpty
    private String comment;

}
