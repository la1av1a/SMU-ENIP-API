package com.smu.smuenip.infrastructure.util.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.smu.smuenip.S3MockConfig;
import com.smu.smuenip.infrastructure.util.cloudFront.CloudFrontApi;
import io.findify.s3mock.S3Mock;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;

@Import(S3MockConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class S3ApiTest {

    @Value("${amazon.bucketName}")
    private String BUCKET_NAME;
    @Autowired
    private S3Api s3Api;
    @Autowired
    private AmazonS3 amazonS3;
    @Mock
    private CloudFrontApi cloudFrontApi;

    @AfterAll
    static void tearDown(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) {
        amazonS3.shutdown();
        s3Mock.stop();
    }

    @BeforeAll
    void setUp(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) {
        s3Mock.start();
        amazonS3.createBucket(BUCKET_NAME);
    }

    @Test
    void uploadImageToS3() throws IOException {
        //given
        String path = "src/test/resources/image/IMG_20230327_135556.JPG";
        String contentType = "image/jpeg";
        String dirname = "test";

        MockMultipartFile file = new MockMultipartFile("file", "IMG_20230327_135556.JPG",
            contentType, path.getBytes());

        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME,
            dirname + "/" + file.getOriginalFilename(),
            file.getInputStream(), null);

        //when
        Mockito.when(cloudFrontApi.generateCloudFrontSignedURL(Mockito.anyString(),
                Mockito.any()))
            .thenReturn("https://d1j4kxkzpsfj0.cloudfront.net/test/IMG_20230327_135556.JPG");
        s3Api.uploadImageToS3(dirname, file, file.getOriginalFilename());

        //then
        Assertions.assertThat(putObjectRequest.getBucketName()).isEqualTo(BUCKET_NAME);

        String url = amazonS3.getUrl(BUCKET_NAME, file.getName()).toString();
        System.out.println(url);
        Assertions.assertThat(url).contains(file.getName());
    }

    @Test
    void deleteImageFromS3() {
        //given
        String path = "src/test/resources/image/IMG_20230327_135556.JPG";
        String contentType = "image/jpeg";
        String dirname = "test";

        MockMultipartFile file = new MockMultipartFile("file", "IMG_20230327_135556.JPG",
            contentType, path.getBytes());

        //when
        s3Api.deleteImageFromS3(dirname, file.getOriginalFilename());

        //then
        Assertions.assertThat(
                amazonS3.doesObjectExist(BUCKET_NAME, dirname + "/" + file.getName()))
            .isFalse();
    }
}