package com.smu.smuenip.domain.image.service;

import com.smu.smuenip.infrastructure.util.Image.ImageUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(SpringExtension.class)
class ImageServiceTest {

    @Test
    void testBase64ToMultipartFile() throws IOException {
        // given
        String base64Image = new String(
            Files.readAllBytes(Paths.get("src/test/resources/base64/base64Test.txt")));

        // when
        MultipartFile multipartFile = ImageUtils.base64ToMultipartFile(base64Image);

        // then
        Assertions.assertThat(multipartFile).isNotNull();
        Assertions.assertThat(multipartFile.getContentType()).isEqualTo("jpeg");

        byte[] expectedBytes = Base64.getMimeDecoder().decode(base64Image.split(",")[1]);
        byte[] actualBytes = multipartFile.getBytes();
        Assertions.assertThat(expectedBytes.length).isEqualTo(actualBytes.length);

        for (int i = 0; i < expectedBytes.length; i++) {
            Assertions.assertThat(expectedBytes[i]).isEqualTo(actualBytes[i]);
        }
    }
}
