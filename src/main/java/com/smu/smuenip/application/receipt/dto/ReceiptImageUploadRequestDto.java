package com.smu.smuenip.application.receipt.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReceiptImageUploadRequestDto implements Serializable {

    @NotEmpty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String date;

    @NotEmpty
    private String image;
}
