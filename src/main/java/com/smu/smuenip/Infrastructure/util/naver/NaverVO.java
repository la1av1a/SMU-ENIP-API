package com.smu.smuenip.Infrastructure.util.naver;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class NaverVO {

    @Value("${naver.search.clientId}")
    private String clientId;

    @Value("${naver.search.secretKey}")
    private String secretKey;


}
