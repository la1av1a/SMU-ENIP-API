package com.smu.smuenip.domain.receipt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OcrDataDto {

    private final String name;
    private final String count;
    private final String price;
}
