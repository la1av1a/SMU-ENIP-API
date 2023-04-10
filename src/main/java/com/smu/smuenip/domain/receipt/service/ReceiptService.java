package com.smu.smuenip.domain.receipt.service;

import com.smu.smuenip.Infrastructure.config.exception.BadRequestException;
import com.smu.smuenip.Infrastructure.config.exception.UnExpectedErrorException;
import com.smu.smuenip.application.user.dto.UserReceiptResponseDto;
import com.smu.smuenip.application.user.dto.UserSetCommentRequestDto;
import com.smu.smuenip.domain.image.Receipt;
import com.smu.smuenip.domain.image.ReceiptRepository;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.meesagesDetail.MessagesFail;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final UserRepository userRepository;

    public Receipt saveProductInfo(String imageUrl, Long userId) {
        User user = findUserByUserId(userId);

        Receipt receipt = Receipt.builder()
            .imageUrl(imageUrl)
            .user(user)
            .build();

        return receiptRepository.save(receipt);
    }

    private User findUserByUserId(Long userId) {
        return userRepository.findUserById(userId).orElseThrow(
            () -> new UnExpectedErrorException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    public List<UserReceiptResponseDto> findReceiptsByDate(LocalDate date, Long userId,
        Pageable pageable) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        User user = findUserById(userId);
        Page<Receipt> receiptPages = receiptRepository.findReceiptsByCreatedDate(year, month, day,
            user, pageable);

        return entityToDto(receiptPages);
    }

    @Transactional
    public void setComment(UserSetCommentRequestDto requestDto, Long userId) {
        Receipt receipt = receiptRepository.findReceiptByIdAndUserId(requestDto.getReceiptId(),
                userId)
            .orElseThrow(
                () -> new BadRequestException(MessagesFail.RECEIPT_NOT_FOUND.getMessage()));
        receipt.setComment(requestDto.getComment());
    }

    private User findUserById(Long userId) {
        return userRepository.findUserById(userId)
            .orElseThrow(() -> new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }

    private List<UserReceiptResponseDto> entityToDto(Page<Receipt> receipts) {
        return receipts.getContent().stream().map(receipt -> UserReceiptResponseDto.builder()
                .id(receipt.getId())
                .imageUrl(receipt.getImageUrl())
                .comment(receipt.getComment())
                .createdDate(receipt.getPurchasedDate())
                .build())
            .collect(Collectors.toList());
    }
}
