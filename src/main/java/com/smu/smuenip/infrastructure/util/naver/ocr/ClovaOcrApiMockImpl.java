package com.smu.smuenip.infrastructure.util.naver.ocr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.smuenip.infrastructure.config.exception.UnExpectedErrorException;
import com.smu.smuenip.infrastructure.util.naver.ocr.dto.OcrRequestDto;
import com.smu.smuenip.infrastructure.util.naver.ocr.dto.OcrResponseDto;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

/**
 * 가짜로 Api
 */
@Service
@RequiredArgsConstructor
public class ClovaOcrApiMockImpl implements ClovaOcrApi {

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    public OcrResponseDto callNaverOcr(OcrRequestDto.Images images) {
        try {
            Resource resource = resourceLoader.getResource("classpath:json/ocr2.json");
            InputStream inputStream = resource.getInputStream();
            String jsonContent = new String(FileCopyUtils.copyToByteArray(inputStream));
            OcrResponseDto ocrResponseDto = objectMapper.readValue(jsonContent,
                OcrResponseDto.class);
            return ocrResponseDto;
        } catch (IOException e) {
            throw new UnExpectedErrorException("Failed to read JSON file");
        }
    }
}
