package com.smu.smuenip.application.recycle.dto;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class RecycledImageResponseDto {

    private final LocalDate date;
    private final Long userId;
    private final Long recycledImageId;
    private final String image;
    private final boolean isChecked;
    private final boolean isApproved;

    public RecycledImageResponseDto(LocalDate date, Long userId,
        Long recycledImageId, String image,
        boolean isChecked, boolean isApproved) {
        this.date = date;
        this.userId = userId;
        this.recycledImageId = recycledImageId;
        this.image = image;
        this.isChecked = isChecked;
        this.isApproved = isApproved;
    }
}
