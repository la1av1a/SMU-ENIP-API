package com.smu.smuenip.infrastructure.util.naver.ocr.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OcrRequestDto {

    private String version;
    private String requestId;
    private long timestamp;
    private Images[] images;

    @Builder
    public OcrRequestDto(String version, String requestId, long timestamp, Images[] images) {
        this.version = version;
        this.requestId = requestId;
        this.timestamp = timestamp;
        this.images = images;
    }

    @Getter
    public static class Images {

        private String format;
        private String data;
        private String name;

        public Images(String format, String data, String name) {
            this.format = format;
            this.data = data;
            this.name = name;
        }
    }
}
