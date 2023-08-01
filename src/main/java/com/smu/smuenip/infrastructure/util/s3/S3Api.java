package com.smu.smuenip.infrastructure.util.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Api {

    private final AmazonS3 s3Client;

    @Value("${amazon.bucketName}")
    private String bucketName;

    public void uploadImageToS3(String dirname, MultipartFile multipartFile, String fileName) {
        
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            s3Client.putObject(bucketName, dirname + "/" + fileName, multipartFile.getInputStream(),
                metadata);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("MultipartFile 업로드 중 문제가 발생했습니다.", e);
        }
    }

    public void deleteImageFromS3(String dirname, String fileName) {
        s3Client.deleteObject(bucketName, dirname + "/" + fileName);
    }

}
