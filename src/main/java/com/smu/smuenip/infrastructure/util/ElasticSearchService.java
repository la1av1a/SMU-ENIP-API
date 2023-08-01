package com.smu.smuenip.infrastructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesFail;
import com.smu.smuenip.infrastructure.config.exception.UnExpectedErrorException;
import com.smu.smuenip.infrastructure.util.elasticSearch.ElSearchRequestDto;
import com.smu.smuenip.infrastructure.util.elasticSearch.ElSearchResponseDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticSearchService {

    private final ObjectMapper objectMapper;
    @Value("${el.search.baseUrl}")
    private String baseUrl;
    @Value("${el.search.path}")
    private String path;
    @Value("${el.search.auth}")
    private String authorization;
    private MultiValueMap<String, String> header;

    public ElSearchResponseDto searchProductWeight(String name) {

        ElSearchRequestDto requestDto = new ElSearchRequestDto();

        ElSearchRequestDto.Query query = new ElSearchRequestDto.Query();
        ElSearchRequestDto.Bool bool = new ElSearchRequestDto.Bool();
        List<ElSearchRequestDto.Must> mustList = new ArrayList<>();

        ElSearchRequestDto.Must must = new ElSearchRequestDto.Must();
        ElSearchRequestDto.Match match = new ElSearchRequestDto.Match();
        match.setName(name);
        log.info(name);

        must.setMatch(match);
        mustList.add(must);
        bool.setMust(mustList);
        query.setBool(bool);
        requestDto.setQuery(query);

        String json = null;
        try {
            json = objectMapper.writeValueAsString(requestDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new UnExpectedErrorException(MessagesFail.UNEXPECTED_ERROR.getMessage());
        }

        log.info("Request Body: {}", json);

        return WebClient.create(baseUrl)
            .post()
            .uri(uriBuilder -> uriBuilder
                .path(path)
                .build())
            .headers(httpHeaders -> httpHeaders.addAll(header))
            .bodyValue(json)
            .retrieve()
            .bodyToMono(ElSearchResponseDto.class).block();
    }

    @PostConstruct
    private void setHeader() {
        header = new LinkedMultiValueMap<>();
        header.add("Authorization", "basic " + authorization);
        header.add("Content-Type", "application/json");
    }
}
