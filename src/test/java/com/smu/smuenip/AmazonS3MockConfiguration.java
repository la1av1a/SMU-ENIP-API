package com.smu.smuenip;

import com.amazonaws.services.s3.AmazonS3;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AmazonS3MockConfiguration {

    @Bean
    public AmazonS3 amazonS3() {
        return Mockito.mock(AmazonS3.class);
    }
}
