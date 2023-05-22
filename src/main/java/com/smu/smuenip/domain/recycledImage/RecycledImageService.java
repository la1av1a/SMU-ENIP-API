package com.smu.smuenip.domain.recycledImage;

import com.smu.smuenip.application.purchasedItem.dto.RecycledImageUploadRequestDto;
import com.smu.smuenip.domain.purchasedItem.model.PurchasedItem;
import com.smu.smuenip.domain.purchasedItem.service.PurchasedItemService;
import com.smu.smuenip.infrastructure.util.Image.ImageUtils;
import com.smu.smuenip.infrastructure.util.s3.S3Api;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RecycledImageService {

    private final RecycledImageRepository recycledImageRepository;
    private final PurchasedItemService purchasedItemService;
    private final S3Api s3Api;

    @Transactional
    public void RecycledImageUpload(RecycledImageUploadRequestDto requestDto) {

        MultipartFile imageMultiPartFile = ImageUtils.base64ToMultipartFile(
                requestDto.getImage());
        MultipartFile resizedImage = ImageUtils.resizeImage(imageMultiPartFile);
        String imageUrl = s3Api.uploadImageToS3(resizedImage,
                imageMultiPartFile.getOriginalFilename());

        PurchasedItem purchasedItem = purchasedItemService.findPurchasedItemById(
                requestDto.getItemId());

        RecycledImage recycledImage = createRecycledImage(imageUrl, purchasedItem);
        recycledImageRepository.save(recycledImage);
    }

    private RecycledImage createRecycledImage(String imageUrl, PurchasedItem purchasedItem) {
        return RecycledImage.builder()
                .recycledImageUrl(imageUrl)
                .uploadDate(LocalDate.now())
                .purchasedItem(purchasedItem)
                .isApproved(false)
                .isChecked(false)
                .build();
    }
}
