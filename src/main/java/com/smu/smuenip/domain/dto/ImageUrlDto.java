package com.smu.smuenip.domain.dto;

import com.smu.smuenip.domain.receipt.model.Receipt;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ImageUrlDto {

    private final String imageUrl;
    private final Receipt receipt;

    @Builder
    public ImageUrlDto(String imageUrl, Receipt receipt) {
        this.imageUrl = imageUrl;
        this.receipt = receipt;
    }
}
