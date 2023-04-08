package com.smu.smuenip.domain.image.service;


import com.smu.smuenip.Infrastructure.util.Image.ImageUtils;
import com.smu.smuenip.domain.dto.ImageURLDTO;
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

    public ImageURLDTO uploadImage(String encodedImage, Long userId) {

        String localFilePath = null;
        String imageURL = null;
        try {
            MultipartFile image = imageService.base64ToMultipartFile(encodedImage);
            MultipartFile resizedImage = imageUtils.resizeImage(image);
            localFilePath = imageService.saveImageInLocal(resizedImage);
            imageURL = imageService.uploadImageToS3(localFilePath,
                image.getOriginalFilename());
            imageService.saveProductInfo(imageURL, userId);
        } finally {
            imageUtils.deleteLocalSavedImage(localFilePath);
        }

        return ImageURLDTO.builder()
            .imageURL(imageURL)
            .localFilePath(localFilePath)
            .build();
    }
}
