package com.smu.smuenip.domain.image.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(SpringExtension.class)
class ReceiptServiceTest {

    @InjectMocks
    ImageService imageService;

    @Test
    void base64ToMultipartFile() throws IOException {
        String imagePath = "src/test/resources/image/IMG_20230327_135556.JPG";

        String base64Image = null;
        try {
            base64Image = encodeImageTo64(imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //when
        MultipartFile image = imageService.base64ToMultipartFile(base64Image);

        //then
        Assertions.assertThat(image.getInputStream()).isNotEmpty();
        System.out.println(image);
    }

    String encodeImageTo64(String imagePath) throws IOException {
        Path path = Paths.get(imagePath);
        byte[] imageBytes = Files.readAllBytes(path);
        return "data:image/jpg;base64," + Base64.getEncoder().encodeToString(imageBytes);
    }
}
