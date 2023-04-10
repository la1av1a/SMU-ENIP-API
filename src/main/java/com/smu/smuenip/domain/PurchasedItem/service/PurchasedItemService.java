package com.smu.smuenip.domain.PurchasedItem.service;

import com.smu.smuenip.Infrastructure.config.exception.UnExpectedErrorException;
import com.smu.smuenip.Infrastructure.util.naver.ItemVO;
import com.smu.smuenip.Infrastructure.util.naver.PurchasedItemVO;
import com.smu.smuenip.Infrastructure.util.naver.search.ClovaShoppingSearchingAPI;
import com.smu.smuenip.application.purchasedItem.dto.PurchasedItemResponseDto;
import com.smu.smuenip.domain.Category.model.Category;
import com.smu.smuenip.domain.Category.service.CategoryService;
import com.smu.smuenip.domain.PurchasedItem.model.PurchasedItem;
import com.smu.smuenip.domain.PurchasedItem.model.PurchasedItemRepository;
import com.smu.smuenip.domain.image.Receipt;
import com.smu.smuenip.domain.image.service.ImageService;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.meesagesDetail.MessagesFail;
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
    private final UserRepository userRepository;
    private final ImageService imageService;

    @Transactional
    public void savePurchasedItem(PurchasedItemVO purchasedItemDTO, Receipt receipt,
        ClovaShoppingSearchingAPI clovaShoppingSearchingAPI,
        CategoryService categoryService, Long userId) {

        ItemVO itemVO = clovaShoppingSearchingAPI.callShoppingApi(purchasedItemDTO.getName());
        Category category = categoryService.getCategory(itemVO);
        String imageUrl = itemVO.getItems().get(0).getImage();
        User user = getUser(userId);

        PurchasedItem purchasedItem = PurchasedItem.builder()
            .receipt(receipt)
            .itemName(purchasedItemDTO.getName())
            .imageUrl(imageUrl)
            .user(user)
            .itemPrice(Integer.parseInt(purchasedItemDTO.getPrice()))
            .itemCount(Integer.parseInt(purchasedItemDTO.getCount()))
            .category(category)
            .build();

        purchasedItemRepository.save(purchasedItem);
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

    private User getUser(Long id) {
        return userRepository.findUserById(id).orElseThrow(
            () -> new UnExpectedErrorException(MessagesFail.UNEXPECTED_ERROR.getMessage()));
    }

    private List<PurchasedItemResponseDto> entityPageToDto(Page<PurchasedItem> purchasedItemPage) {
        return purchasedItemPage.getContent().stream()
            .map(purchasedItem -> PurchasedItemResponseDto.builder()
                .itemId(purchasedItem.getPurchasedItemId())
                .itemName(purchasedItem.getItemName())
                .userId(purchasedItem.getUser().getId())
                .imageUrl(purchasedItem.getImageUrl())
                .itemPrice(purchasedItem.getItemPrice())
                .itemCount(purchasedItem.getItemCount())
                .isRecycled(purchasedItem.isRecycled())
                .build()
            ).collect(Collectors.toList());
    }
}
