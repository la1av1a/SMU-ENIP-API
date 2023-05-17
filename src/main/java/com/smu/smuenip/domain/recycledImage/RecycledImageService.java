package com.smu.smuenip.domain.recycledImage;

import com.smu.smuenip.Infrastructure.config.exception.BadRequestException;
import com.smu.smuenip.Infrastructure.util.Image.ImageUtils;
import com.smu.smuenip.application.purchasedItem.dto.RecycledImageUploadRequestDto;
import com.smu.smuenip.domain.PurchasedItem.model.PurchasedItem;
import com.smu.smuenip.domain.PurchasedItem.model.PurchasedItemRepository;
import com.smu.smuenip.domain.image.service.ImageService;
import com.smu.smuenip.enums.meesagesDetail.MessagesFail;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RecycledImageService {

    private final RecycledImageRepository recycledImageRepository;
    private final PurchasedItemRepository purchasedItemRepository;
    private final ImageUtils imageUtils;
    private final ImageService imageService;

    @Transactional
    public void RecycledImageUpload(RecycledImageUploadRequestDto requestDto) {

        MultipartFile imageMultiPartFile = imageService.base64ToMultipartFile(
            requestDto.getImage());
        MultipartFile resizedImage = imageUtils.resizeImage(imageMultiPartFile);
        String imageUrl = imageService.uploadImageToS3(resizedImage,
            imageMultiPartFile.getOriginalFilename());

        PurchasedItem purchasedItem = purchasedItemRepository.findById(requestDto.getItemId())
            .orElseThrow(() -> new BadRequestException(MessagesFail.UNEXPECTED_ERROR.getMessage()));

        RecycledImage recycledImage = RecycledImage.builder()
            .recycledImageUrl(imageUrl)
            .uploadDate(LocalDate.now())
            .purchasedItem(purchasedItem)
            .isApproved(false)
            .isChecked(false)
            .build();

        recycledImageRepository.save(recycledImage);
    }
}
