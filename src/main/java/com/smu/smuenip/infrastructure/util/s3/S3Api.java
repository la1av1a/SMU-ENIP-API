package com.smu.smuenip.infrastructure.util.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudfront.AmazonCloudFront;
import com.amazonaws.services.cloudfront.AmazonCloudFrontClientBuilder;
import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.amazonaws.services.cloudfront.model.CreateInvalidationRequest;
import com.amazonaws.services.cloudfront.model.InvalidationBatch;
import com.amazonaws.services.cloudfront.model.Paths;
import com.amazonaws.services.cloudfront.util.SignerUtils.Protocol;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.smu.smuenip.infrastructure.util.s3.vo.S3Vo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Api {
    private final S3Vo s3Vo;

    public String uploadImageToS3(MultipartFile multipartFile, String fileName) {
        AmazonS3 s3Client = buildS3Client();

        // Upload file to S3 bucket
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            PutObjectRequest putObjectRequest = new PutObjectRequest(s3Vo.getBucketName(), fileName, multipartFile.getInputStream(), metadata);
            s3Client.putObject(putObjectRequest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("MultipartFile 업로드 중 문제가 발생했습니다.", e);
        }

        // Generate signed URL
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);

        String signedUrl = generateCloudFrontSignedURL(fileName, expiration);
        return signedUrl;
    }

    private AmazonS3 buildS3Client() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(s3Vo.getAccessKey(), s3Vo.getSecretKey());
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(s3Vo.getRegion())
                .build();
    }

    public void deleteImageFromS3(String fileName) {
        AmazonS3 s3Client = buildS3Client();

        // Delete file from S3 bucket
        s3Client.deleteObject(s3Vo.getBucketName(), fileName);

        // Invalidate CloudFront cache
        invalidateCloudFrontCache(fileName);
    }

    private String generateCloudFrontSignedURL(String fileName, java.util.Date expiration) {
        String privateKeyFilePath = s3Vo.getPrivateKeyFilePath();
        String keyPairId = s3Vo.getKeyPairId();

        String signedUrl = null;
        try {
            signedUrl = CloudFrontUrlSigner.getSignedURLWithCannedPolicy(
                    Protocol.https, s3Vo.getDistributionDomain(), new File(privateKeyFilePath),
                    fileName, keyPairId, expiration);
        } catch (InvalidKeySpecException | IOException e) {
            e.printStackTrace();
        }
        return signedUrl;
    }

    private void invalidateCloudFrontCache(String fileName) {
        AmazonCloudFront cloudFrontClient = AmazonCloudFrontClientBuilder.defaultClient();
        CreateInvalidationRequest invalidationBatchRequest = new CreateInvalidationRequest(
                s3Vo.getDistributionDomain(),
                new InvalidationBatch().withPaths(new Paths().withItems("/" + fileName).withQuantity(1)).withCallerReference(UUID.randomUUID().toString())
        );
        cloudFrontClient.createInvalidation(invalidationBatchRequest);
    }


}
