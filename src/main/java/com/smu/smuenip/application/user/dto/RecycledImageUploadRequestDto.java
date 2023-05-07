package com.smu.smuenip.application.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RecycledImageUploadRequestDto implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDate localDate;
    private String image;
    private Long receiptId;
}
