package com.smu.smuenip.application.purchasedItem.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecycledImageUploadRequestDto {

    @NotEmpty(message = "분리수거 대상 이미지의 id는 비어서는 안 됩니다")
    private Long itemId;

    @NotEmpty(message = "이미지는 비어서는 안 됩니다")
    @Pattern(regexp = "^data:image\\/(?:jpeg|jpg|gif|png);base64,([a-zA-Z0-9+/]+={0,2})$", message = "올바른 Base64 형식 이미지가 아닙니다")
    private String image;

    public RecycledImageUploadRequestDto(Long itemId, String image) {
        this.itemId = itemId;
        this.image = image;
    }
}
