package com.smu.smuenip.infrastructure.util.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Getter
@Configuration
public class S3Config {

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

    @Profile("prod")
    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder
            .standard()
            .withRegion(region)
            .withCredentials(
                new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
            .build();

    }
}
