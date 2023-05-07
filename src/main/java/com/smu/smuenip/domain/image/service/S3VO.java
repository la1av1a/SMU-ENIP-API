package com.smu.smuenip.domain.image.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class S3VO {

    @Value("${amazon.accessKey}")
    private String accessKey;

    @Value("${amazon.secretKey}")
    private String secretKey;

    @Value("${amazon.region}")
    private String region;

    @Value("${amazon.bucketName}")
    private String bucketName;

    @Value("${amazon.distributionDomain}")
    private String distributionDomain;

    @Value("${amazon.keyPairId}")
    private String keyPairId;

    @Value("${amazon.privateKeyFilePath}")
    private String privateKeyFilePath;

}
