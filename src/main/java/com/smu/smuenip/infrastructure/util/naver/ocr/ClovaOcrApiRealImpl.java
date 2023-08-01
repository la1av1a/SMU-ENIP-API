package com.smu.smuenip.infrastructure.util.naver.ocr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesFail;
import com.smu.smuenip.infrastructure.config.exception.UnExpectedErrorException;
import com.smu.smuenip.infrastructure.util.naver.ocr.VO.ClovaOcrVo;
import com.smu.smuenip.infrastructure.util.naver.ocr.dto.OcrRequestDto;
import com.smu.smuenip.infrastructure.util.naver.ocr.dto.OcrRequestDto.Images;
import com.smu.smuenip.infrastructure.util.naver.ocr.dto.OcrResponseDto;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * 실제로 외부 API 호출
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClovaOcrApiRealImpl implements ClovaOcrApi {

    private final ClovaOcrVo clovaOcrVo;
    private final ObjectMapper objectMapper;
    private MultiValueMap<String, String> header;

    @PostConstruct
    public void setHeader() {
        header = new LinkedMultiValueMap<>();
        header.add("X-OCR-SECRET", clovaOcrVo.getSecret());
        header.add("Content-Type", "application/json");
    }

    @Override
    public OcrResponseDto callNaverOcr(Images images) {

        OcrRequestDto ocrRequestDto = OcrRequestDto.builder()
            .requestId(clovaOcrVo.getRequestId())
            .timestamp(clovaOcrVo.getTimestamp())
            .version(clovaOcrVo.getVersion())
            .timestamp(clovaOcrVo.getTimestamp())
            .images(new Images[]{images})
            .build();
        log.info("ocrRequestDto: {}", ocrRequestDto.getRequestId());
        log.info("ocrRequestDto: {}", ocrRequestDto.getImages()[0].getFormat());

        String json = null;

        try {
            json = objectMapper.writeValueAsString(ocrRequestDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            throw new UnExpectedErrorException(MessagesFail.UNEXPECTED_ERROR.getMessage());
        }

        Mono<OcrResponseDto> result = WebClient.create(clovaOcrVo.getBaseUrl())
            .post()
            .uri(uriBuilder -> uriBuilder
                .path(clovaOcrVo.getPath())
                .build())
            .headers(httpHeaders -> httpHeaders.addAll(header))
            .bodyValue(json)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                log.info("4xx error");
                return Mono.error(
                    new UnExpectedErrorException(MessagesFail.UNEXPECTED_ERROR.getMessage()));
            })
            .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                log.info("5xx error");
                return Mono.error(
                    new UnExpectedErrorException(MessagesFail.UNEXPECTED_ERROR.getMessage()));
            }).bodyToMono(OcrResponseDto.class);

        return result.block();
    }
}
