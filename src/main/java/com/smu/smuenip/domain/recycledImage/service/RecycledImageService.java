package com.smu.smuenip.domain.recycledImage.service;

import com.smu.smuenip.application.recycle.dto.RecycledImageResponseDto;
import com.smu.smuenip.domain.recycledImage.entity.RecycledImageRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecycledImageService {

    private final RecycledImageRepository recycledImageRepository;

    @Transactional(readOnly = true)
    public List<RecycledImageResponseDto> getRecycledImageListForUser(Long userId, LocalDate date,
        Boolean isRecycled) {

        List<RecycledImageResponseDto> recycledImageList;
        if (date == null) {
            if (isRecycled == null) {
                recycledImageList = recycledImageRepository.findRecycledResponseDtoByUserId(userId);
            } else {
                recycledImageList = recycledImageRepository.findRecycledResponseDtoByCheckedAndApprovedAndUserId(
                    userId, isRecycled);
            }
        } else {
            if (isRecycled == null) {
                recycledImageList = recycledImageRepository.findRecycledResponseDtoByUserIdAndDate(
                    userId, date);
            } else {
                recycledImageList = recycledImageRepository.findRecycledResponseDtoByUserIdAndDateAndIsRecycled(
                    userId, date, isRecycled);
            }
        }

        return recycledImageList;
    }

    @Transactional(readOnly = true)
    public boolean isExistsByItemId(Long itemId) {
        return recycledImageRepository.existsRecycledImageByPurchasedItemPurchasedItemId(itemId);
    }

    @Transactional(readOnly = true)
    public List<RecycledImageResponseDto> getRecycledImageListForAdmin(LocalDate date,
        Boolean isRecycled) {

        List<RecycledImageResponseDto> recycledImageList;
        if (date == null) {
            if (isRecycled == null) {
                recycledImageList = recycledImageRepository.findAllRecycledImages();
            } else {
                recycledImageList = recycledImageRepository.findRecycledImagesByCheckedAndApproved(
                    isRecycled);
            }
        } else {
            if (isRecycled == null) {
                recycledImageList = recycledImageRepository.findRecycledImagesByDate(date);
            } else {
                recycledImageList = recycledImageRepository.findRecycledImagesByDateAndIsRecycled(
                    date, isRecycled);
            }
        }

        return recycledImageList;
    }
}
