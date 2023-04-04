package com.smu.smuenip.Infrastructure.util.naver.ocr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.smuenip.Infrastructure.util.naver.ocr.ocrResult.OcrResultDTO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 추후 구현 예정
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClovaOCRAPI {

    private final ObjectMapper objectMapper;
    private final String API_URL = "https://openapi.naver.com/v1/vision/ocr";

    public OcrResultDTO callOcrApi(String filePath) {

//        WebClient webClient = WebClient.create(API_URL);
//
//        return webClient.get()
//            .accept(MediaType.APPLICATION_JSON)
//            .retrieve()
//            .bodyToMono(OcrResult.class);

        String jsonContent2 = null;
        OcrResultDTO ocrResult = null;

        try {
            jsonContent2 = new String(Files.readAllBytes(Paths.get("json/ocr.json")));
            log.info("json : {}", jsonContent2);
            ocrResult = objectMapper.readValue(jsonContent2, OcrResultDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ocrResult;
    }
}
