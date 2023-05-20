package com.smu.smuenip.infrastructure.util.naver.ocr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesFail;
import com.smu.smuenip.infrastructure.config.exception.UnExpectedErrorException;
import com.smu.smuenip.infrastructure.util.naver.ocr.OcrRequestDto.Images;
import com.smu.smuenip.infrastructure.util.naver.ocr.VO.ClovaOcrVo;
import com.smu.smuenip.infrastructure.util.naver.ocr.ocrResult.OcrResultDto2;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


/**
 * 추후 구현 예정
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClovaOCRAPI {

    private final ClovaOcrVo clovaOcrVo;
    private final ObjectMapper objectMapper;
    private MultiValueMap<String, Object> header;

    @PostConstruct
    public void setHeader() {
        header = new LinkedMultiValueMap<>();
        header.add("X-OCR-SECRET", clovaOcrVo.getSecret());
        header.add("Content-Type", "application/json");
    }

    public void callNaverOcr(Images images) {

        OcrRequestDto ocrRequestDto = OcrRequestDto.builder()
            .requestId(clovaOcrVo.getRequestId())
            .timestamp(clovaOcrVo.getTimestamp())
            .version(clovaOcrVo.getVersion())
            .timestamp(clovaOcrVo.getTimestamp())
            .images(new Images[]{images})
            .build();

        String json = null;

        try {
            json = objectMapper.writeValueAsString(ocrRequestDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            throw new UnExpectedErrorException(MessagesFail.UNEXPECTED_ERROR.getMessage());
        }

        Mono<OcrResultDto2> result = WebClient.create()
            .post()
            .uri(uriBuilder -> uriBuilder
                .scheme("https")
                .host(clovaOcrVo.getHost())
                .build())
            .bodyValue(json)
            .retrieve()
            .bodyToMono(OcrResultDto2.class).doOnSuccess(ocrResultDto2 -> {
                log.info("ocrResultDto2 = {}",
                    ocrResultDto2.getImages()[0].receipt.result.subResults.get(0).items.get(
                        0).name.formatted.value);
            });
    }
}
