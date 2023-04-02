package com.smu.smuenip.Infrastructure.util.naver;

import com.smu.smuenip.domain.user.repository.UserRepository;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClovaShoppingSearchingApi {


    private final UserRepository userRepository;
    private final NaverVo naverVo;


    MultiValueMap<String, String> headers = new HttpHeaders();

    public String searchNaverShoppingByItem(String item) {
        String decodedItem = URLDecoder.decode(item, StandardCharsets.UTF_8);

        WebClient client = WebClient.builder()
            .baseUrl("https://openapi.naver.com")
            .defaultHeader("Host", "openapi.naver.com")
            .defaultHeader("User-Agent", "curl/7.49.1")
            .defaultHeader("Accept", "*/*")
            .defaultHeader("X-Naver-Client-Id", naverVo.getClientId())
            .defaultHeader("X-Naver-Client-Secret", naverVo.getSecretKey())
            .build();

        Mono<ItemInfo> result = client.get().uri(uriBuilder -> uriBuilder
                .path("/v1/search/shop.json")
                .queryParam("query", decodedItem)
                .build()
            )
            .headers(headers -> headers.addAll(headers))
            .retrieve()
            .bodyToMono(ItemInfo.class)
            .publishOn(Schedulers.boundedElastic())
            .doOnSuccess(itemInfo -> {
                log.info(itemInfo.getItems().get(0).getTitle());
                log.info(itemInfo.getItems().get(1).getTitle());
            });

        result.subscribe(
            itemInfo -> log.info(itemInfo.getLastBuildDate()), // onNext
            error -> log.error("Error: " + error.getMessage()), // onError
            () -> log.info("Processing completed") // onComplete
        );

        return "yoho";
    }
}
