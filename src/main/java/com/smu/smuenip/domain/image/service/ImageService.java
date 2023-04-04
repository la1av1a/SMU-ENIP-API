package com.smu.smuenip.domain.image.service;

import com.smu.smuenip.Infrastructure.config.exception.UnExpectedErrorException;
import com.smu.smuenip.Infrastructure.util.naver.PurchasedItemDTO;
import com.smu.smuenip.Infrastructure.util.naver.ocr.ocrResult.OcrResultDTO;
import com.smu.smuenip.Infrastructure.util.s3.S3API;
import com.smu.smuenip.enums.meesagesDetail.MessagesFail;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3API s3API;

    public String uploadImageToS3(String filePath, String fileName) {
        return s3API.uploadImageToS3(filePath, fileName);
    }

    public List<PurchasedItemDTO> extractPurchasedInfo(OcrResultDTO ocrResult) {
        List<OcrResultDTO.Item> items = ocrResult.getResult().getSubResults().get(0).getItems();
        List<PurchasedItemDTO> list = new ArrayList<>();
        for (OcrResultDTO.Item item : items) {
            if (item.getName().formatted == null) {
                continue;
            }
            String name = item.getName().formatted.value;
            String count =
                item.getCount().formatted == null ? null : item.getCount().formatted.value;
            String price = item.getPriceInfo().price.formatted == null ? null
                : item.getPriceInfo().price.formatted.value;

            list.add(new PurchasedItemDTO(name, count, price));
        }

        return list;

    }

    public String saveImageInLocal(MultipartFile image) {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(image.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("이미지를 읽는 도중 오류가 발생했습니다.", e);
        }

        if (image == null) {
            throw new RuntimeException("이미지 파일이 올바르지 않거나 지원되지 않는 형식입니다.");
        }
        String outputFilePath = "uploads/" + image.getOriginalFilename();
        log.info(outputFilePath);
        File outputFile = new File(outputFilePath);

        try {
            ImageIO.write(bufferedImage, getFileExtension(outputFile), outputFile);
        } catch (IOException e) {
            throw new UnExpectedErrorException(MessagesFail.IMAGE_UPLOAD_FAIL.getMessage());
        }

        return outputFilePath;
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int index = fileName.lastIndexOf('.');
        return index > 0 ? fileName.substring(index + 1) : "";
    }

    private void saveProductInfo() {
        //TODO : DB에 상품명 , 가격 , 카테고리 저장
    }
}
