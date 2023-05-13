package com.smu.smuenip.domain.image.service;


import com.smu.smuenip.Infrastructure.util.Image.ImageUtils;
import com.smu.smuenip.domain.dto.ImageURLDTO;
import com.smu.smuenip.domain.image.Receipt;
import com.smu.smuenip.domain.receipt.service.ReceiptService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class ImageProcessingService {

    private final ImageService imageService;
    private final ImageUtils imageUtils;
    private final ReceiptService receiptService;

    public ImageURLDTO uploadImage(String encodedImage, Long userId, LocalDate purchasedDate) {

        String localFilePath = null;
        MultipartFile image = imageService.base64ToMultipartFile(encodedImage);
        MultipartFile resizedImage = imageUtils.resizeImage(image);
        String imageUrl = imageService.uploadImageToS3(resizedImage,
            image.getOriginalFilename());
        Receipt receipt = receiptService.saveProductInfo(imageUrl, userId, purchasedDate);

        return ImageURLDTO.builder()
            .imageURL(imageUrl)
            .localFilePath(localFilePath)
            .receipt(receipt)
            .build();
    }
}
