package com.smu.smuenip.domain.receipt.service;

import com.smu.smuenip.Infrastructure.config.exception.UnExpectedErrorException;
import com.smu.smuenip.domain.receipt.model.Receipt;
import com.smu.smuenip.domain.receipt.repository.ReceiptRepository;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.meesagesDetail.MessagesFail;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final UserRepository userRepository;


    @Transactional
    public Receipt saveReceiptAndPurchasedItems(String filePath, Long userId) {

        User user = userRepository.findUserById(userId)
            .orElseThrow(() -> new UnExpectedErrorException(
                MessagesFail.USER_NOT_FOUND.getMessage()));

        Receipt receipt = Receipt.builder()
            .user(user)
            .receipt_url(filePath)
            .build();

        return receiptRepository.save(receipt);
    }

}
