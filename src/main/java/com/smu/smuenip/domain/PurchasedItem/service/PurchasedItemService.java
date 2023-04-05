package com.smu.smuenip.domain.PurchasedItem.service;

import com.smu.smuenip.Infrastructure.config.exception.UnExpectedErrorException;
import com.smu.smuenip.Infrastructure.config.redis.TokenInfo;
import com.smu.smuenip.Infrastructure.util.naver.ItemDTO;
import com.smu.smuenip.Infrastructure.util.naver.PurchasedItemDTO;
import com.smu.smuenip.Infrastructure.util.naver.search.ClovaShoppingSearchingAPI;
import com.smu.smuenip.domain.Category.model.Category;
import com.smu.smuenip.domain.Category.service.CategoryService;
import com.smu.smuenip.domain.PurchasedItem.model.PurchasedItem;
import com.smu.smuenip.domain.PurchasedItem.model.PurchasedItemRepository;
import com.smu.smuenip.domain.receipt.model.Receipt;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.meesagesDetail.MessagesFail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchasedItemService {

    private final PurchasedItemRepository purchasedItemRepository;
    private final UserRepository userRepository;

    @Transactional
    public void savePurchasedItem(PurchasedItemDTO purchasedItemDTO, Receipt receipt,
                                  ClovaShoppingSearchingAPI clovaShoppingSearchingAPI,
                                  CategoryService categoryService, TokenInfo tokenInfo) {

        ItemDTO itemDTO = clovaShoppingSearchingAPI.callShoppingApi(purchasedItemDTO.getName());
        Category category = categoryService.getCategory(itemDTO);

        User user = getUser(Long.valueOf(tokenInfo.getId()));

        PurchasedItem purchasedItem = PurchasedItem.builder()
                .receipt(receipt)
                .itemName(purchasedItemDTO.getName())
                .user(user)
                .itemPrice(Integer.parseInt(purchasedItemDTO.getPrice()))
                .itemCount(Integer.parseInt(purchasedItemDTO.getCount()))
                .category(category)
                .build();

        log.info(purchasedItem.getItemName());
        purchasedItemRepository.save(purchasedItem);
    }

    private User getUser(Long id) {
        return userRepository.findUserById(id).orElseThrow(
                () -> new UnExpectedErrorException(MessagesFail.UNEXPECTED_ERROR.getMessage()));
    }
}
