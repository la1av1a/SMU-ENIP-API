package com.smu.smuenip.Infrastructure.util.naver.ocr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.smuenip.Infrastructure.util.naver.ocr.ocrResult.OcrResultDto2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * 추후 구현 예정
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClovaOCRAPI {

    private final ObjectMapper objectMapper;
    private final String API_URL = "https://openapi.naver.com/v1/vision/ocr";

    public OcrResultDto2 callOcrApi(String filePath) {

//        WebClient webClient = WebClient.create(API_URL);
//
//        return webClient.get()
//            .accept(MediaType.APPLICATION_JSON)
//            .retrieve()
//            .bodyToMono(OcrResult.class);

        String jsonContent2 = null;
        OcrResultDto2 ocrResult = null;

        try {
            jsonContent2 = new String(Files.readAllBytes(Paths.get("json/ocr.json")));
            ocrResult = objectMapper.readValue(jsonContent2, OcrResultDto2.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ocrResult;
    }
}
