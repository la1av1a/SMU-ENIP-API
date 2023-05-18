package com.smu.smuenip.domain.dto;

import com.smu.smuenip.domain.receipt.model.Receipt;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ImageURLDTO {

    private final String localFilePath;
    private final String imageURL;
    private final Receipt receipt;

    @Builder
    public ImageURLDTO(String localFilePath, String imageURL, Receipt receipt) {
        this.localFilePath = localFilePath;
        this.imageURL = imageURL;
        this.receipt = receipt;
    }
}
