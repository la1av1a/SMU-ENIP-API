package com.smu.smuenip.domain.receipt.service;

import com.smu.smuenip.Infrastructure.util.naver.PurchasedItemDTO;
import com.smu.smuenip.Infrastructure.util.naver.search.ClovaShoppingSearchingAPI;
import com.smu.smuenip.application.user.dto.UserReceiptUploadRequestDTO;
import com.smu.smuenip.domain.Category.service.CategoryService;
import com.smu.smuenip.domain.PurchasedItem.service.PurchasedItemService;
import com.smu.smuenip.domain.dto.ImageURLDTO;
import com.smu.smuenip.domain.externalAPI.APIService;
import com.smu.smuenip.domain.image.service.ImageProcessingService;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiptProcessingService {

    private final ClovaShoppingSearchingAPI clovaShoppingSearchingAPI;
    private final PurchasedItemService purchasedItemService;
    private final CategoryService categoryService;
    private final ImageProcessingService imageProcessingService;
    private final APIService apiService;

    @Transactional
    public void processReceipt(UserReceiptUploadRequestDTO requestDTO,
        Long userId) {

        ImageURLDTO imageURLDTO = imageProcessingService.uploadImage(
            requestDTO.getImage(), userId);
        List<PurchasedItemDTO> purchasedItemDTOList = apiService.callAPI(
            imageURLDTO.getLocalFilePath());

        for (PurchasedItemDTO purchasedItemDTO : purchasedItemDTOList) {
            purchasedItemService.savePurchasedItem(purchasedItemDTO, imageURLDTO.getReceipt(),
                clovaShoppingSearchingAPI, categoryService, userId);
        }
    }
}
