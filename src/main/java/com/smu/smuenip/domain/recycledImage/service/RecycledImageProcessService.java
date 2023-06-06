package com.smu.smuenip.domain.recycledImage.service;

import com.smu.smuenip.application.purchasedItem.dto.RecycledImageUploadRequestDto;
import com.smu.smuenip.application.recycle.dto.RecycledImageResponseDto;
import com.smu.smuenip.domain.purchasedItem.model.PurchasedItem;
import com.smu.smuenip.domain.purchasedItem.service.PurchasedItemProcessService;
import com.smu.smuenip.domain.recycledImage.entity.RecycledImage;
import com.smu.smuenip.domain.recycledImage.entity.RecycledImageRepository;
import com.smu.smuenip.enums.Role;
import com.smu.smuenip.infrastructure.config.exception.UnExpectedErrorException;
import com.smu.smuenip.infrastructure.util.Image.ImageUtils;
import com.smu.smuenip.infrastructure.util.s3.S3Api;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RecycledImageProcessService {

    private final RecycledImageRepository recycledImageRepository;
    private final PurchasedItemProcessService purchasedItemProcessService;
    private final S3Api s3Api;
    private final RecycledImageService recycledImageService;

    @Transactional
    public void RecycledImageUpload(RecycledImageUploadRequestDto requestDto) {

        String resizedImageUrl = null;
        String originalImageUrl = null;

        try {
            MultipartFile imageMultiPartFile = ImageUtils.base64ToMultipartFile(
                requestDto.getImage());
            MultipartFile resizedImage = ImageUtils.resizeImage(imageMultiPartFile);
            resizedImageUrl = s3Api.uploadImageToS3(resizedImage,
                imageMultiPartFile.getOriginalFilename());
            originalImageUrl = s3Api.uploadImageToS3(imageMultiPartFile,
                imageMultiPartFile.getOriginalFilename() + "-origin");

            PurchasedItem purchasedItem = purchasedItemProcessService.findPurchasedItemById(
                requestDto.getItemId());

            RecycledImage recycledImage = createRecycledImage(resizedImageUrl, originalImageUrl,
                purchasedItem);
            recycledImageRepository.save(recycledImage);
        } catch (Exception e) {
            s3Api.deleteImageFromS3(resizedImageUrl);
            s3Api.deleteImageFromS3(originalImageUrl);
            throw new UnExpectedErrorException((e.getMessage()));
        }
    }

    @Transactional(readOnly = true)
    public List<RecycledImageResponseDto> getRecycledItems(Long userId, Role role, LocalDate date,
        Boolean isRecycled) {

        if (role == Role.ROLE_USER) {
            return recycledImageService.getRecycledImageListForUser(userId, date, isRecycled);
        }

        return recycledImageService.getRecycledImageListForAdmin(date, isRecycled);
    }

    @Transactional(readOnly = true)
    public RecycledImage findRecycledById(Long recycledImageId) {
        return recycledImageRepository.findById(recycledImageId)
            .orElseThrow(() -> new UnExpectedErrorException("존재하지 않는 이미지입니다."));
    }

    private RecycledImage createRecycledImage(String imageUrl, String originalImageUrl,
        PurchasedItem purchasedItem) {
        return RecycledImage.builder()
            .recycledImageUrl(imageUrl)
            .originalImageUrl(originalImageUrl)
            .uploadDate(LocalDate.now())
            .purchasedItem(purchasedItem)
            .isApproved(false)
            .isChecked(false)
            .build();
    }
}
