package com.smu.smuenip.domain.image.service;


import com.smu.smuenip.Infrastructure.util.Image.ImageUtils;
import com.smu.smuenip.application.user.dto.UserImageUploadRequestDto;
import com.smu.smuenip.domain.dto.ImageURLDTO;
import com.smu.smuenip.domain.image.Receipt;
import com.smu.smuenip.domain.receipt.service.ReceiptService;
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

    public ImageURLDTO uploadImage(String encodedImage, Long userId) {

        String localFilePath = null;
        String imageUrl = null;
        Receipt receipt = null;
        try {
            MultipartFile image = imageService.base64ToMultipartFile(encodedImage);
            MultipartFile resizedImage = imageUtils.resizeImage(image);
            localFilePath = imageService.saveImageInLocal(resizedImage);
            imageUrl = imageService.uploadImageToS3(localFilePath,
                image.getOriginalFilename());
            receipt = receiptService.saveProductInfo(imageUrl, userId);
        } finally {
            imageUtils.deleteLocalSavedImage(localFilePath);
        }

        return ImageURLDTO.builder()
            .imageURL(imageUrl)
            .localFilePath(localFilePath)
            .receipt(receipt)
            .build();
    }

    public void uploadRecycledImage(UserImageUploadRequestDto requestDto) {
        MultipartFile image = imageService.base64ToMultipartFile(requestDto.getImage());
        MultipartFile resizedImage = imageUtils.resizeImage(image);
        String localFilePath = imageService.saveImageInLocal(resizedImage);
        String uploadedImageUrl = imageService.uploadImageToS3(localFilePath,
            image.getOriginalFilename());
    }
}
