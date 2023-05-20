package com.smu.smuenip.domain.purchasedItem.service;

import com.smu.smuenip.Infrastructure.config.exception.UnExpectedErrorException;
import com.smu.smuenip.Infrastructure.util.naver.ItemDto;
import com.smu.smuenip.Infrastructure.util.naver.PurchasedItemDto;
import com.smu.smuenip.Infrastructure.util.naver.search.ClovaShoppingSearchingAPI;
import com.smu.smuenip.application.purchasedItem.dto.PurchasedItemResponseDto;
import com.smu.smuenip.domain.category.model.Category;
import com.smu.smuenip.domain.category.service.CategoryService;
import com.smu.smuenip.domain.purchasedItem.model.PurchasedItem;
import com.smu.smuenip.domain.purchasedItem.model.PurchasedItemRepository;
import com.smu.smuenip.domain.receipt.model.Receipt;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.serivce.UserService;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesFail;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchasedItemService {

    private final PurchasedItemRepository purchasedItemRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ClovaShoppingSearchingAPI clovaShoppingSearchingAPI;

    @Transactional
    public void savePurchasedItem(PurchasedItemDto purchasedItemDTO, Receipt receipt, Long userId,
        LocalDate purchased_date) {

        ItemDto itemDto = clovaShoppingSearchingAPI.callShoppingApi(purchasedItemDTO.getName());
        Category category = categoryService.findCategory(itemDto);
        String imageUrl = itemDto.getItems().get(0).getImage();
        User user = userService.findUserById(userId);

        PurchasedItem purchasedItem = createPurchasedItem(receipt, purchasedItemDTO.getName(),
            imageUrl,
            user,
            Integer.parseInt(purchasedItemDTO.getPrice()),
            Integer.parseInt(purchasedItemDTO.getCount()),
            category,
            purchased_date);

        purchasedItemRepository.save(purchasedItem);
    }

    private PurchasedItem createPurchasedItem(Receipt receipt, String itemName, String imageUrl,
        User user, int itemPrice, int itemCount, Category category, LocalDate purchasedDate) {

        return PurchasedItem.builder()
            .receipt(receipt)
            .itemName(itemName)
            .imageUrl(imageUrl)
            .user(user)
            .itemPrice(itemPrice)
            .itemCount(itemCount)
            .category(category)
            .purchasedDate(purchasedDate)
            .build();
    }

    public List<PurchasedItemResponseDto> getPurchasedItems(LocalDate date, Long userId,
        Pageable pageable) {

        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        Page<PurchasedItem> purchasedItemPage = purchasedItemRepository.findPurchasedItemsByCreatedDate(
            year, month, day, userId, pageable);

        return entityPageToDto(purchasedItemPage);
    }

    public PurchasedItem findPurchasedItemById(Long purchasedItemId) {
        return purchasedItemRepository.findById(purchasedItemId).orElseThrow(
            () -> new UnExpectedErrorException(MessagesFail.UNEXPECTED_ERROR.getMessage()));
    }

    private List<PurchasedItemResponseDto> entityPageToDto(Page<PurchasedItem> purchasedItemPage) {
        return purchasedItemPage.getContent().stream()
            .map(purchasedItem -> PurchasedItemResponseDto.builder()
                .purchasedItemId(purchasedItem.getPurchasedItemId())
                .receiptId(purchasedItem.getReceipt().getId())
                .trashAmount(0) // TODO 추후 구현
                .expenditureCost(purchasedItem.getItemPrice() + "원")
                .date(purchasedItem.getReceipt().getPurchasedDate())
                .build()
            ).collect(Collectors.toList());
    }
}
