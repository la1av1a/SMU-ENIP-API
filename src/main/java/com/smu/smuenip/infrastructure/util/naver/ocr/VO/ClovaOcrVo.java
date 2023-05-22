package com.smu.smuenip.infrastructure.util.naver.ocr.VO;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ClovaOcrVo {

    @Value("${naver.ocr.baseUrl}")
    private String baseUrl;
    @Value("${naver.ocr.path}")
    private String path;
    @Value("${naver.ocr.secret}")
    private String secret;
    @Value("${naver.ocr.version}")
    private String version;
    @Value("${naver.ocr.requestId}")
    private String requestId;
    @Value("${naver.ocr.timestamp}")
    private long timestamp;
}
