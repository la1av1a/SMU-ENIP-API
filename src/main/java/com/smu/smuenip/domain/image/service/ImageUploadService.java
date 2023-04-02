package com.smu.smuenip.domain.image.service;

import static com.smu.smuenip.Infrastructure.util.Image.ImageUtils.deleteLocalSavedImage;
import static com.smu.smuenip.Infrastructure.util.Image.ImageUtils.resizeImage;

import com.smu.smuenip.domain.amazon.S3Service;
import com.smu.smuenip.domain.user.repository.UserRepository;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageUploadService {


    private final UserRepository userRepository;
    private final S3Service s3Service;

    public void uploadImage(MultipartFile file) {
        String filePath = resizeImage(file);
        log.info("filePath : {}", filePath);
        String imageUrl = s3Service.uploadImageToS3(filePath, file.getOriginalFilename());

        //TODO : 클로바 OCR API 호출 후 결과 반환 { { 상품명 , 가격 } , { 상품명 , 가격 } , ... }
        //TODO : OCR 결과를 바탕으로 NAVER 검색 API 호출하여 카테고리 추출
        //TODO : DB에 상품명 , 가격 , 카테고리 저장

        log.info("imageUrl : {}", imageUrl);
        deleteLocalSavedImage(filePath);
    }

    private void test(BufferedImage bufferedImage) {
        try {
            String outputFilePath = "uploads/";
            File outputFile = new File(outputFilePath);

            boolean res = ImageIO.write(bufferedImage, getFileExtension(outputFile), outputFile);
            log.info("result : {}", res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int index = fileName.lastIndexOf('.');
        return index > 0 ? fileName.substring(index + 1) : "";
    }

//    public String saveComment() {
//
//    }

}
