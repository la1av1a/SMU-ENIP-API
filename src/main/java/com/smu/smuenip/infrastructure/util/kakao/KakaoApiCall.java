package com.smu.smuenip.infrastructure.util.kakao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class KakaoApiCall {

    private final String KAKAO_API_HOST = "kauth.kakao.com";
    private final String KAKAO_API_TOKEN_PATH = "/oauth/token";
    private final String KAKAO_API_USER_PATH = "/v2/user/me";
    private final String KAKAO_API_KEY = "6016a54669f886eceb4043fb823bb544";
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    public KakaoApiCall() {
        params.add("grant_type", "authorization_code");
        params.add("client_id", KAKAO_API_KEY);
        params.add("redirect_uri", "http://localhost:8080/user/oauth/login/kakao");
    }

    public KakaoLoginDto callKakaoLogin(String code) {

        Mono<KakaoLoginDto> result = WebClient.create()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(KAKAO_API_HOST)
                        .path(KAKAO_API_TOKEN_PATH)
                        .queryParams(params)
                        .queryParam("code", code)
                        .build())
                .retrieve()
                .bodyToMono(KakaoLoginDto.class);

        return result.block();
    }
}

