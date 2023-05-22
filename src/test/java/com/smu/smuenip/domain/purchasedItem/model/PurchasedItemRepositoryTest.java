package com.smu.smuenip.domain.purchasedItem.model;

import com.smu.smuenip.domain.receipt.model.Receipt;
import com.smu.smuenip.domain.receipt.model.ReceiptRepository;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@DataJpaTest
class PurchasedItemRepositoryTest {

    User savedUser;
    Receipt savedReceipt;
    @Autowired
    private PurchasedItemRepository purchasedItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReceiptRepository receiptRepository;

    @BeforeEach
    void setUp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse("2023-04-01", formatter);

        User user = User.builder()
                .email("test1234@gmail.com")
                .role(Role.ROLE_USER)
                .score(0)
                .build();

        savedUser = userRepository.save(user);

        Receipt receipt = Receipt.builder()
                .purchasedDate(date)
                .user(savedUser)
                .build();

        savedReceipt = receiptRepository.save(receipt);

        LocalDate date2 = LocalDate.parse("2023-04-02", formatter);
        savePurchasedItem(date, 1000, 1, "콜라", savedUser, savedReceipt);
        savePurchasedItem(date, 2000, 2, "사이다", savedUser, savedReceipt);
        savePurchasedItem(date, 3000, 3, "환타", savedUser, savedReceipt);
        savePurchasedItem(date, 4000, 4, "피자", savedUser, savedReceipt);
    }

    @Test
    void findPurchasedItemsByCreatedDate() {
        // given
        int givenYear = 2023;
        int givenMonth = 4;
        int givenDay = 1;
        int expectedSize = 4;
        // when
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<PurchasedItem> purchasedItemPage = purchasedItemRepository.findPurchasedItemsByCreatedDate(
                givenYear, givenMonth, givenDay, savedUser.getUserId(), pageRequest);
        // then
        Assertions.assertThat(purchasedItemPage.getNumberOfElements()).isEqualTo(expectedSize);
    }

    void savePurchasedItem(LocalDate date, int itemPrice, int itemCount, String itemName,
                           User user, Receipt receipt) {
        PurchasedItem purchasedItem = createPurchasedItem(date, itemPrice, itemCount, itemName,
                user, receipt);
        purchasedItemRepository.save(purchasedItem);
    }

    PurchasedItem createPurchasedItem(LocalDate date, int itemPrice, int itemCount,
                                      String itemName, User user, Receipt receipt) {
        return PurchasedItem.builder()
                .itemPrice(itemPrice)
                .itemCount(itemCount)
                .itemName(itemName)
                .purchasedDate(date)
                .user(user)
                .receipt(receipt)
                .build();
    }
}

