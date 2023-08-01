package com.smu.smuenip.application.receipt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class UserReceiptResponseDto {

    private Long id;
    private String imageUrl;
    private String originalImageUrl;
    private String comment;
    private LocalDate createdDate;
    private boolean isRecycled;

    @Builder
    public UserReceiptResponseDto(Long id, String imageUrl, String originalImageUrl, String comment,
        LocalDate createdDate,
        boolean isRecycled) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.originalImageUrl = originalImageUrl;
        this.comment = comment;
        this.createdDate = createdDate;
        this.isRecycled = isRecycled;
    }
}
