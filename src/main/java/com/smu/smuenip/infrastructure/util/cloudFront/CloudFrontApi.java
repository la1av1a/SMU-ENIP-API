package com.smu.smuenip.infrastructure.util.cloudFront;

import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.amazonaws.services.cloudfront.util.SignerUtils.Protocol;
import java.io.File;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CloudFrontApi {

    @Value("${amazon.distributionDomain}")
    private String distributionDomain;

    @Value("${amazon.keyPairId}")
    private String keyPairId;

    @Value("${amazon.privateKeyFilePath}")
    private String privateKeyFilePath;

    public String generateCloudFrontSignedURL(String fileName, Date expiration) {

        String signedUrl = null;
        try {
            signedUrl = CloudFrontUrlSigner.getSignedURLWithCannedPolicy(
                Protocol.https, distributionDomain, new File(privateKeyFilePath),
                fileName, keyPairId, expiration);
        } catch (InvalidKeySpecException | IOException e) {
            e.printStackTrace();
        }
        return signedUrl;
    }

    public Date setExpiration(long expiringTime) {
        Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += expiringTime;
        expiration.setTime(expTimeMillis);

        return expiration;
    }
}
