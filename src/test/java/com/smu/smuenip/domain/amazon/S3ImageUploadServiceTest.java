package com.smu.smuenip.domain.amazon;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.junit.jupiter.api.Test;

class S3ImageUploadServiceTest {

    @Test
    void upload() {
        AmazonS3 s3ClientMock = mock(AmazonS3.class);
        when(s3ClientMock.putObject(any(PutObjectRequest.class))).thenReturn(
            mock(PutObjectResult.class));

    }
}
