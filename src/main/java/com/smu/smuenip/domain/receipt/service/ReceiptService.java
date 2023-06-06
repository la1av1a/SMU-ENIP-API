package com.smu.smuenip.domain.receipt.service;

import com.smu.smuenip.application.Receipt.dto.ReceiptSetCommentRequestDto;
import com.smu.smuenip.application.Receipt.dto.UserReceiptResponseDto;
import com.smu.smuenip.domain.purchasedItem.service.PurchasedItemProcessService;
import com.smu.smuenip.domain.receipt.OcrDataDto;
import com.smu.smuenip.domain.receipt.model.Receipt;
import com.smu.smuenip.domain.receipt.model.ReceiptRepository;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesFail;
import com.smu.smuenip.infrastructure.config.exception.BadRequestException;
import com.smu.smuenip.infrastructure.config.exception.UnExpectedErrorException;
import com.smu.smuenip.infrastructure.util.ElasticSearchService;
import com.smu.smuenip.infrastructure.util.Image.ImageUtils;
import com.smu.smuenip.infrastructure.util.elasticSearch.ElSearchResponseDto;
import com.smu.smuenip.infrastructure.util.naver.ocr.ClovaOcrApi;
import com.smu.smuenip.infrastructure.util.naver.ocr.OcrRequestDto;
import com.smu.smuenip.infrastructure.util.naver.ocr.dto.OcrResponseDto;
import com.smu.smuenip.infrastructure.util.s3.S3Api;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final UserRepository userRepository;
    private final ClovaOcrApi clovaOCRAPI;
    private final PurchasedItemProcessService purchasedItemProcessService;
    private final S3Api s3Api;
    private final ElasticSearchService elasticSearchService;

    public void uploadReceipt(String encodedImage, LocalDate purchasedDate, Long userId) {
        try {
            MultipartFile image = ImageUtils.base64ToMultipartFile(encodedImage);

            String originalImageUrl = s3Api.uploadImageToS3(image,
                image.getOriginalFilename() + "-origin");

            MultipartFile resizedImage = ImageUtils.resizeImage(image);
            String url = s3Api.uploadImageToS3(resizedImage, image.getOriginalFilename());

            Receipt receipt = saveReceipt(url, originalImageUrl, userId, purchasedDate);

            Mono<OcrResponseDto> ocrMono = clovaOCRAPI.callNaverOcr(
                new OcrRequestDto.Images(image.getContentType(),
                    encodedImage.split(",", 2)[1], image.getOriginalFilename()));

            OcrResponseDto ocrResponseDto = ocrMono.block();
            assert ocrResponseDto != null;
            List<OcrDataDto> ocrDataDtoList = extractOcrData(ocrResponseDto);

            List<Mono<ElSearchResponseDto>> monos = ocrDataDtoList.parallelStream()
                .filter(ocrDataDto -> ocrDataDto != null && ocrDataDto.getName() != null)
                .map(ocrDataDto -> elasticSearchService.searchProductWeight(
                    removeClosingPattern(ocrDataDto.getName())))
                .collect(Collectors.toList());

            Mono.zip(monos, array -> array)
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess(array -> {
                    for (int i = 0; i < array.length; i++) {
                        ElSearchResponseDto elSearchResponseDto = (ElSearchResponseDto) array[i];
                        OcrDataDto ocrDataDto = ocrDataDtoList.get(i);
                        purchasedItemProcessService.savePurchasedItem(ocrDataDto,
                            elSearchResponseDto, receipt, userId, purchasedDate);
                    }
                })
                .subscribe();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String removeClosingPattern(String originalStr) {
        return originalStr.replaceAll(".*\\)", "");
    }


    @Transactional(readOnly = true)
    public List<UserReceiptResponseDto> findReceiptsByDate(LocalDate date, Long userId,
        Pageable pageable) {

        if (date == null) {
            return entityToDto(
                receiptRepository.findReceiptsByUserUserIdOrderByPurchasedDateDesc(userId,
                    pageable));
        }

        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        User user = findUserById(userId);
        Page<Receipt> receiptPages = receiptRepository.findReceiptsByCreatedDate(year, month, day,
            user, pageable);

        return entityToDto(receiptPages);
    }

    @Transactional
    public void setComment(ReceiptSetCommentRequestDto requestDto, Long userId) {
        Receipt receipt = receiptRepository.findReceiptByIdAndUserUserId(requestDto.getReceiptId(),
                userId)
            .orElseThrow(
                () -> new BadRequestException(MessagesFail.RECEIPT_NOT_FOUND.getMessage()));
        receipt.setComment(requestDto.getComment());
    }

    public Receipt saveReceipt(String imageUrl, String originalImageUrl, Long userId,
        LocalDate purchasedDate) {
        User user = findUserByUserId(userId);

        Receipt receipt = Receipt.builder()
            .imageUrl(imageUrl)
            .originalImageUrl(originalImageUrl)
            .user(user)
            .purchasedDate(purchasedDate)
            .build();

        return receiptRepository.save(receipt);
    }

    private User findUserById(Long userId) {
        return userRepository.findUserByUserId(userId)
            .orElseThrow(() -> new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }

    private List<UserReceiptResponseDto> entityToDto(Page<Receipt> receipts) {
        return receipts.getContent().stream().map(receipt -> UserReceiptResponseDto.builder()
                .id(receipt.getId())
                .imageUrl(receipt.getImageUrl())
                .originalImageUrl(receipt.getOriginalImageUrl())
                .comment(receipt.getComment())
                .createdDate(receipt.getPurchasedDate())
                .build())
            .collect(Collectors.toList());
    }

    private User findUserByUserId(Long userId) {
        return userRepository.findUserByUserId(userId).orElseThrow(
            () -> new UnExpectedErrorException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }

    private List<OcrDataDto> extractOcrData(OcrResponseDto ocrResponseDto) {
        return ocrResponseDto.getImages()[0].receipt.result.subResults.get(
                0).items.parallelStream()
            .map(item -> new OcrDataDto(item.name == null ? "null" : item.name.formatted.value,
                item.count == null ? "null" : item.count.formatted.value,
                item.price.price.formatted == null ? "null" : item.price.price.formatted.value))
            .collect(Collectors.toList());
    }

}
