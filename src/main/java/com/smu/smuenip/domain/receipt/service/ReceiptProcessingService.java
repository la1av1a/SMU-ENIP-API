package com.smu.smuenip.domain.receipt.service;

import com.smu.smuenip.Infrastructure.util.naver.PurchasedItemVO;
import com.smu.smuenip.Infrastructure.util.naver.search.ClovaShoppingSearchingAPI;
import com.smu.smuenip.application.user.dto.RecycledImageUploadRequestDto;
import com.smu.smuenip.domain.Category.service.CategoryService;
import com.smu.smuenip.domain.PurchasedItem.service.PurchasedItemService;
import com.smu.smuenip.domain.dto.ImageURLDTO;
import com.smu.smuenip.domain.externalAPI.APIService;
import com.smu.smuenip.domain.image.service.ImageProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
    public void processReceipt(RecycledImageUploadRequestDto requestDTO, Long userId) {
        ImageURLDTO imageURLDTO = imageProcessingService.uploadImage(
                requestDTO.getImage(), userId);
        List<PurchasedItemVO> purchasedItemDTOList = apiService.callAPI(
                imageURLDTO.getLocalFilePath());

        for (PurchasedItemVO purchasedItemDTO : purchasedItemDTOList) {
            purchasedItemService.savePurchasedItem(purchasedItemDTO, imageURLDTO.getReceipt(),
                    clovaShoppingSearchingAPI, categoryService, userId);
        }
    }
}
