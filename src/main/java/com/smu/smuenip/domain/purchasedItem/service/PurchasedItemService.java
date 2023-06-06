package com.smu.smuenip.domain.purchasedItem.service;

import com.smu.smuenip.application.purchasedItem.dto.PurchasedItemResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchasedItemService {

    private final PurchasedItemRepository purchasedItemRepository;
    private final UserService userService;

    @Transactional
    public void savePurchasedItem(OcrDataDto ocrDataDto, ElSearchResponseDto elSearchResponseDto,
        Receipt receipt,
        Long userId, LocalDate purchased_date) {
        User user = userService.findUserById(userId);

        PurchasedItem purchasedItem = createPurchasedItem(
            receipt,
            ocrDataDto.getName(),
            receipt.getImageUrl(),
            user,
            ocrDataDto.getPrice().equals("null") ? 0 : Integer.parseInt(ocrDataDto.getPrice()),
            ocrDataDto.getCount().equals("null") ? 0 : Integer.parseInt(ocrDataDto.getCount()),
            elSearchResponseDto.getTrashAmount(),
            elSearchResponseDto.getCategory(),
            purchased_date);

        purchasedItemRepository.save(purchasedItem);
    }

    @Transactional(readOnly = true)
    public List<PurchasedItemResponseDto> getNotRecycledItems(Long userId) {
        List<PurchasedItem> purchasedItemPage = purchasedItemRepository.findNotRecycledItemsByUserUserId(
            userId);

        return entityToDto(purchasedItemPage);
    }

    private PurchasedItem createPurchasedItem(Receipt receipt, String itemName, String imageUrl,
        User user, int itemPrice, int itemCount, int trashAmount, String category,
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
    public List<PurchasedItemResponseDto> getAllPurchasedItems(LocalDate date, Long userId,
        Pageable pageable) {
        if (date == null) {
            Page<PurchasedItem> purchasedItemPage = purchasedItemRepository.findPurchasedItemByUserUserId(
                userId, pageable);
            return entityToDto(purchasedItemPage.get().collect(Collectors.toList()));
        }

        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        Page<PurchasedItem> purchasedItemPage = purchasedItemRepository.findPurchasedItemsByCreatedDate(
            year, month, day, userId, pageable);
        return entityToDto(purchasedItemPage.get().collect(Collectors.toList()));
    }


    public PurchasedItem findPurchasedItemById(Long purchasedItemId) {
        return purchasedItemRepository.findById(purchasedItemId).orElseThrow(
            () -> new UnExpectedErrorException(MessagesFail.UNEXPECTED_ERROR.getMessage()));
    }

    private List<PurchasedItemResponseDto> entityToDto(List<PurchasedItem> purchasedItemPage) {
        return purchasedItemPage.stream()
            .map(purchasedItem -> PurchasedItemResponseDto.builder()
                .purchasedItemId(purchasedItem.getPurchasedItemId())
                .purchasedItemExampleImage(purchasedItem.getImageUrl())
                .receiptId(purchasedItem.getReceipt().getId())
                .trashAmount(purchasedItem.getTrashAmount())
                .expenditureCost(purchasedItem.getItemPrice() + "원")
                .date(purchasedItem.getReceipt().getPurchasedDate())
                .isRecycled(purchasedItem.getRecycledImage() != null)
                .category(purchasedItem.getCategory())
                .build()
            ).collect(Collectors.toList());
    }
}
