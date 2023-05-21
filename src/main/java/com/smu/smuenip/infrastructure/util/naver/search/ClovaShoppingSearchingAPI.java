package com.smu.smuenip.infrastructure.util.naver.search;

import com.smu.smuenip.infrastructure.util.naver.ItemDto;
import com.smu.smuenip.infrastructure.util.naver.NaverVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClovaShoppingSearchingAPI {

    private final NaverVO naverVo;

    public ItemDto callShoppingApi(String item) {
        String decodedItem = URLDecoder.decode(item, StandardCharsets.UTF_8);

        WebClient client = WebClient.builder()
                .baseUrl("https://openapi.naver.com")
                .defaultHeader("Host", "openapi.naver.com")
                .defaultHeader("User-Agent", "curl/7.49.1")
                .defaultHeader("Accept", "*/*")
                .defaultHeader("X-Naver-Client-Id", naverVo.getClientId())
                .defaultHeader("X-Naver-Client-Secret", naverVo.getSecretKey())
                .build();

        Mono<ItemDto> result = client.get().uri(uriBuilder -> uriBuilder
                        .path("/v1/search/shop.json")
                        .queryParam("query", decodedItem)
                        .build()
                )
                .headers(headers -> headers.addAll(headers))
                .retrieve()
                .bodyToMono(ItemDto.class)
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess(itemDto -> log.info("쇼핑 api 호출 -완료-"));
        return result.block();
    }
}
