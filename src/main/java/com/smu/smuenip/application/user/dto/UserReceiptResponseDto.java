package com.smu.smuenip.application.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

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
