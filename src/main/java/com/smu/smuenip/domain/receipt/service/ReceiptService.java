package com.smu.smuenip.domain.receipt.service;

import com.smu.smuenip.Infrastructure.config.exception.UnExpectedErrorException;
import com.smu.smuenip.domain.image.Receipt;
import com.smu.smuenip.domain.image.ReceiptRepository;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.meesagesDetail.MessagesFail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final UserRepository userRepository;

    public void saveProductInfo(String imageUrl, Long userId) {
        User user = findUserByUserId(userId);

        Receipt receipt = Receipt.builder()
            .imageUrl(imageUrl)
            .user(user)
            .build();
        receiptRepository.save(receipt);
    }

    private User findUserByUserId(Long userId) {
        return userRepository.findUserById(userId).orElseThrow(
            () -> new UnExpectedErrorException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }
}
