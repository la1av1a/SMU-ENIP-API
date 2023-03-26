package com.smu.smuenip.domain.amazon;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.amazonaws.services.cloudfront.util.SignerUtils.Protocol;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.smu.smuenip.domain.amazon.vo.S3Vo;
import java.io.File;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3ImageUploadService {

    private final S3Vo s3Vo;

    public void uploadImageToS3(String filePath, String fileName) {

        String accessKey = s3Vo.getAccessKey();
        String secretKey = s3Vo.getSecretKey();
        String region = s3Vo.getRegion();
        String bucketName = s3Vo.getBucketName();

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

        // Generate signed URL (valid for 1 year)
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
//        expTimeMillis += 1000 * 60 * 60 * 24 * 365; // 1 year
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);

        String distributionDomain = s3Vo.getDistributionDomain();
        String privateKeyFilePath = "./pk-APKA5SBL6IKZWKYOKC5N.pem";
        String keyPairId = s3Vo.getKeyPairId();

        String signedUrl = null;
        try {
            signedUrl = CloudFrontUrlSigner.getSignedURLWithCannedPolicy(
                Protocol.https, distributionDomain, new File(privateKeyFilePath),
                s3Key, keyPairId, expiration);
        } catch (InvalidKeySpecException |
                 IOException e) {
            e.printStackTrace();
        }

        //saveImageURL to <Image> Table
        //TODO
//        save(imageUrl, User);
    }
}
