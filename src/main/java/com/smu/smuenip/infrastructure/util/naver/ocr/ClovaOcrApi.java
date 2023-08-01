package com.smu.smuenip.infrastructure.util.naver.ocr;

import com.smu.smuenip.infrastructure.util.naver.ocr.dto.OcrRequestDto;
import com.smu.smuenip.infrastructure.util.naver.ocr.dto.OcrResponseDto;

public interface ClovaOcrApi {

    OcrResponseDto callNaverOcr(OcrRequestDto.Images images);
}
