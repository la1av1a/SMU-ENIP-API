package com.smu.smuenip.domain.purchasedItem.service;

import com.smu.smuenip.application.purchasedItem.dto.PurchasedItemResponseDto;
import com.smu.smuenip.domain.category.entity.Category;
import com.smu.smuenip.domain.category.service.CategoryService;
import com.smu.smuenip.domain.purchasedItem.model.PurchasedItem;
import com.smu.smuenip.domain.purchasedItem.model.PurchasedItemRepository;
import com.smu.smuenip.domain.receipt.OcrDataDto;
import com.smu.smuenip.domain.receipt.model.Receipt;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.serivce.UserService;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesFail;
import com.smu.smuenip.infrastructure.config.exception.UnExpectedErrorException;
import com.smu.smuenip.infrastructure.util.elasticSearch.ElSearchResponseDto;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchasedItemProcessService {

    private final PurchasedItemRepository purchasedItemRepository;
    private final PurchasedItemService purchasedItemService;
    private final CategoryService categoryService;
    private final UserService userService;

    @Transactional
    public void savePurchasedItem(OcrDataDto ocrDataDto, ElSearchResponseDto elSearchResponseDto,
        Receipt receipt,
        Long userId, LocalDate purchased_date) {
        User user = userService.findUserById(userId);
        Category category = categoryService.getCategoryByCategoryName(
            elSearchResponseDto.getCategory());

        if (category == null) {
            log.info("유효한 상품이 아님");
            return;
        }

        PurchasedItem purchasedItem = createPurchasedItem(
            receipt,
            ocrDataDto.getName(),
            receipt.getImageUrl(),
            user,
            ocrDataDto.getPrice().equals("null") ? 0 : Integer.parseInt(ocrDataDto.getPrice()),
            ocrDataDto.getCount().equals("null") ? 0 : Integer.parseInt(ocrDataDto.getCount()),
            elSearchResponseDto.getTrashAmount(),
            category,
            purchased_date);

        purchasedItemRepository.save(purchasedItem);
    }

    public List<PurchasedItemResponseDto> getRecycledItemsByIsRecycled(Long userId,
        Boolean isRecycled) {

        if (!isRecycled) {
            return entityToDto(purchasedItemService.getNotRecycledItemsByUserUserId(userId));
        }

        return entityToDto(purchasedItemService.getRecycledItemsByUserUserId(userId));
    }

    private PurchasedItem createPurchasedItem(Receipt receipt, String itemName, String imageUrl,
        User user, int itemPrice, int itemCount, int trashAmount, Category category,
        LocalDate purchasedDate) {

        return PurchasedItem.builder()
            .receipt(receipt)
            .itemName(itemName)
            .imageUrl(imageUrl)
            .user(user)
            .itemPrice(itemPrice)
            .itemCount(itemCount)
            .trashAmount(trashAmount)
            .category(category)
            .purchasedDate(purchasedDate)
            .build();
    }

    @Transactional(readOnly = true)
    public List<PurchasedItemResponseDto> getAllPurchasedItems(LocalDate date, Long userId) {
        if (date == null) {
            List<PurchasedItem> purchasedItemList = purchasedItemRepository.findPurchasedItemByUserUserId(
                userId);
            return entityToDto(purchasedItemList);
        }

        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        List<PurchasedItem> purchasedItemList = purchasedItemRepository.findPurchasedItemsByCreatedDate(
            year, month, day, userId);
        return entityToDto(purchasedItemList);
    }


    public PurchasedItem findPurchasedItemById(Long purchasedItemId) {
        return purchasedItemRepository.findById(purchasedItemId).orElseThrow(
            () -> new UnExpectedErrorException(MessagesFail.UNEXPECTED_ERROR.getMessage()));
    }

    private List<PurchasedItemResponseDto> entityToDto(List<PurchasedItem> purchasedItemPage) {
        return purchasedItemPage.stream()
            .filter(purchasedItem -> purchasedItem.getReceipt() != null)
            .map(purchasedItem -> PurchasedItemResponseDto.builder()
                .purchasedItemId(purchasedItem.getPurchasedItemId())
                .categoryImage(purchasedItem.getCategory().getCategoryImage() == null ? null
                    : purchasedItem.getCategory().getCategoryImage())
                .category(purchasedItem.getCategory().getCategoryName())
                .receiptId(purchasedItem.getReceipt().getId())
                .trashAmount(purchasedItem.getTrashAmount())
                .recycleImage(purchasedItem.getCategory().getRecycleImage())
                .name(purchasedItem.getItemName())
                .receiptImage(purchasedItem.getReceipt().getOriginalImageUrl())
                .expenditureCost(purchasedItem.getItemPrice() + "원")
                .date(purchasedItem.getReceipt().getPurchasedDate())
                .isRecycled(purchasedItem.getRecycledImage() != null)
                .build()
            ).collect(Collectors.toList());
    }
}
