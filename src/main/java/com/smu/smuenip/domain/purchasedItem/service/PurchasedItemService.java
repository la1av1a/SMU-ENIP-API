package com.smu.smuenip.domain.purchasedItem.service;

import com.smu.smuenip.domain.purchasedItem.model.PurchasedItem;
import com.smu.smuenip.domain.purchasedItem.model.PurchasedItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchasedItemService {

    private final PurchasedItemRepository purchasedItemRepository;

    @Transactional(readOnly = true)
    public List<PurchasedItem> getRecycledItemsByUserUserId(Long userId) {
        return purchasedItemRepository.findRecycledItemsByUserUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<PurchasedItem> getNotRecycledItemsByUserUserId(Long userId) {
        return purchasedItemRepository.findNotRecycledItemsByUserUserId(
            userId);
    }
}
