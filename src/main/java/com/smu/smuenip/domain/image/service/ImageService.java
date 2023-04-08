package com.smu.smuenip.domain.image.service;

import com.smu.smuenip.Infrastructure.config.exception.UnExpectedErrorException;
import com.smu.smuenip.Infrastructure.util.naver.PurchasedItemDTO;
import com.smu.smuenip.Infrastructure.util.naver.ocr.ocrResult.OcrResultDTO;
import com.smu.smuenip.Infrastructure.util.s3.S3API;
import com.smu.smuenip.domain.image.ReceiptRepository;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.meesagesDetail.MessagesFail;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3API s3API;
    private final ReceiptRepository receiptRepository;
    private final UserRepository userRepository;

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

    public String saveImageInLocal(MultipartFile image) throws UnExpectedErrorException {
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
        File outputFile = new File(outputFilePath);

        try {
            boolean res = ImageIO.write(bufferedImage, getFileExtension(outputFile), outputFile);
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

    public MultipartFile base64ToMultipartFile(String base64) {
        String data = base64.split(",")[1];
        byte[] decoded = Base64.getMimeDecoder().decode(data);
        String contentType = extractContentType(base64);
        String generatedImageName = generateUUID();
        return new MockMultipartFile(generatedImageName,
            generatedImageName + "." + contentType, contentType, decoded);
    }

    private String extractContentType(String base64) {
        Pattern pattern = Pattern.compile("data:image/(.+?);base64");
        Matcher matcher = pattern.matcher(base64);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
