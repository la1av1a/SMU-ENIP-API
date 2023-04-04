package com.smu.smuenip.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ImageURLDTO {

    private final String localFilePath;
    private final String imageURL;

    @Builder
    public ImageURLDTO(String localFilePath, String imageURL) {
        this.localFilePath = localFilePath;
        this.imageURL = imageURL;
    }
}
