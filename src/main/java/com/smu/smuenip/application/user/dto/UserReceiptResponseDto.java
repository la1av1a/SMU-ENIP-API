package com.smu.smuenip.application.user.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserReceiptResponseDto {

    private Long id;
    private String imageUrl;
    private String comment;
    private LocalDate createdDate;
    private boolean isRecycled;

    @Builder
    public UserReceiptResponseDto(Long id, String imageUrl, String comment,
        LocalDate createdDate,
        boolean isRecycled) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.comment = comment;
        this.createdDate = createdDate;
        this.isRecycled = isRecycled;
    }
}
