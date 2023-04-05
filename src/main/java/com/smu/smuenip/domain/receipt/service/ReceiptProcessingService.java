package com.smu.smuenip.domain.receipt.service;

import com.smu.smuenip.Infrastructure.config.redis.CustomUserDetails;
import com.smu.smuenip.Infrastructure.util.naver.PurchasedItemDTO;
import com.smu.smuenip.Infrastructure.util.naver.search.ClovaShoppingSearchingAPI;
import com.smu.smuenip.domain.Category.service.CategoryService;
import com.smu.smuenip.domain.PurchasedItem.service.PurchasedItemService;
import com.smu.smuenip.domain.dto.ImageURLDTO;
import com.smu.smuenip.domain.externalAPI.APIService;
import com.smu.smuenip.domain.image.service.ImageProcessingService;
import com.smu.smuenip.domain.image.service.ImageService;
import com.smu.smuenip.domain.receipt.model.Receipt;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiptProcessingService {

    private final ClovaShoppingSearchingAPI clovaShoppingSearchingAPI;
    private final ImageService imageService;
    private final ReceiptService receiptService;
    private final PurchasedItemService purchasedItemService;
    private final CategoryService categoryService;
    private final ImageProcessingService imageProcessingService;
    private final APIService apiService;

    @Transactional
    public void processReceipt(MultipartFile image, CustomUserDetails customUserDetails) {

        ImageURLDTO imageURLDTO = imageProcessingService.uploadImage(image);

        List<PurchasedItemDTO> purchasedItemDTOList = apiService.callAPI(
            imageURLDTO.getLocalFilePath());

        Receipt receipt = receiptService.saveReceiptAndPurchasedItems(imageURLDTO.getImageURL(),
            Long.valueOf(customUserDetails.getId()));

        for (PurchasedItemDTO purchasedItemDTO : purchasedItemDTOList) {
            purchasedItemService.savePurchasedItem(purchasedItemDTO, receipt,
                clovaShoppingSearchingAPI, categoryService, customUserDetails);
        }

    }
}
