package com.smu.smuenip.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NaverRequestEnum {
    X_OCR_SECRET("X-OCR-SECRET"),
    Content_Type("Content-Type"),
    VERSION("version"),
    REQUEST_ID("requestId"),
    TIMESTAMP("timestamp"),
    IMAGES("images"),
    FORMAT("format"),
    DATA("data"),
    NAME("name");

    private final String value;
}
