package com.smu.smuenip.domain.recycledImage.service;

import com.smu.smuenip.application.purchasedItem.dto.RecycledImageUploadRequestDto;
import com.smu.smuenip.application.recycle.dto.RecycledImageResponseDto;
import com.smu.smuenip.domain.purchasedItem.model.PurchasedItem;
import com.smu.smuenip.domain.purchasedItem.service.PurchasedItemProcessService;
import com.smu.smuenip.domain.recycledImage.entity.RecycledImage;
import com.smu.smuenip.domain.recycledImage.entity.RecycledImageRepository;
import com.smu.smuenip.domain.upload.ImageUploadService;
import com.smu.smuenip.enums.Role;
import com.smu.smuenip.infrastructure.config.exception.BadRequestException;
import com.smu.smuenip.infrastructure.config.exception.UnExpectedErrorException;
import com.smu.smuenip.infrastructure.util.Image.ImageUtils;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecycledImageProcessService {

    private final RecycledImageRepository recycledImageRepository;
    private final PurchasedItemProcessService purchasedItemProcessService;
    private final RecycledImageService recycledImageService;
    private final ImageUploadService imageUploadService;
    private final String DIR_NAME = "recycled";

    @Transactional
    public void RecycledImageUpload(RecycledImageUploadRequestDto requestDto) {

        if (recycledImageService.isExistsByItemId(requestDto.getItemId())) {
            throw new BadRequestException("이미 해당 아이템 대상으로 이미지가 업로드가 되어있습니다.");
        }

        String resizedImageUrl = null;
        String originalImageUrl = null;

        try {
            MultipartFile imageMultiPartFile = ImageUtils.base64ToMultipartFile(
                requestDto.getImage());
            MultipartFile resizedImage = ImageUtils.resizeImage(imageMultiPartFile);
            imageUploadService.uploadImages(requestDto.getImage(), DIR_NAME);

            PurchasedItem purchasedItem = purchasedItemProcessService.findPurchasedItemById(
                requestDto.getItemId());

            RecycledImage recycledImage = createRecycledImage(resizedImageUrl, originalImageUrl,
                purchasedItem);
            recycledImageRepository.save(recycledImage);
        } catch (Exception e) {
//            s3Api.deleteImageFromS3(resizedImageUrl);
//            s3Api.deleteImageFromS3(originalImageUrl);
            throw new UnExpectedErrorException((e.getMessage()));
        }
    }

    @Transactional(readOnly = true)
    public List<RecycledImageResponseDto> getRecycledItems(Long userId, Role role, LocalDate date,
        Boolean isRecycled) {

        if (role == Role.ROLE_USER) {
            log.info("호출");
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
