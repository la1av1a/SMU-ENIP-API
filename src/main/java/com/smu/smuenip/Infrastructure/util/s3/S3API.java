package com.smu.smuenip.Infrastructure.util.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.amazonaws.services.cloudfront.util.SignerUtils.Protocol;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.smu.smuenip.domain.image.service.S3VO;
import java.io.File;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3API {


    private final S3VO s3VO;

    public String uploadImageToS3(String filePath, String fileName) {

        String accessKey = s3VO.getAccessKey();
        String secretKey = s3VO.getSecretKey();
        String region = s3VO.getRegion();
        String bucketName = s3VO.getBucketName();

        // AWS credentials
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        // AmazonS3 client
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
            .withRegion(region)
            .build();

        // File to upload
        File file = new File(filePath);

        // S3 Key (file name)
        String s3Key = fileName;

        // Upload file to S3 bucket
        PutObjectResult result = s3Client.putObject(new PutObjectRequest(bucketName, s3Key, file));

        // Generate signed URL
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);

        String distributionDomain = s3VO.getDistributionDomain();
        String privateKeyFilePath = s3VO.getPrivateKeyFilePath();
        String keyPairId = s3VO.getKeyPairId();

        String signedUrl = null;
        try {
            signedUrl = CloudFrontUrlSigner.getSignedURLWithCannedPolicy(
                Protocol.https, distributionDomain, new File(privateKeyFilePath),
                s3Key, keyPairId, expiration);
        } catch (InvalidKeySpecException |
                 IOException e) {
            e.printStackTrace();
        }
        return signedUrl;
    }

}
