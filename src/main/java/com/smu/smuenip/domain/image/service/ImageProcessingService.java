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

    public ImageURLDTO uploadImage(MultipartFile image) {
        MultipartFile resizedImage = imageUtils.resizeImage(image);
        String localFilePath = imageService.saveImageInLocal(resizedImage);
        String imageURL = imageService.uploadImageToS3(localFilePath, image.getOriginalFilename());

        return ImageURLDTO.builder()
            .imageURL(imageURL)
            .localFilePath(localFilePath)
            .build();
    }
}
