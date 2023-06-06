package com.smu.smuenip.application.purchasedItem;

import com.smu.smuenip.application.purchasedItem.dto.PurchasedItemResponseDto;
import com.smu.smuenip.domain.purchasedItem.service.PurchasedItemProcessService;
import com.smu.smuenip.infrastructure.config.CustomUserDetails;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/item")
@RestController
@RequiredArgsConstructor
public class PurchasedItemController implements PurchasedItemControllerSwagger {

    private final PurchasedItemProcessService purchasedItemProcessService;

    //이름, "분류"
    @Override
    @GetMapping("/list")
    public List<PurchasedItemResponseDto> getPurchasedItems(
        @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @RequestParam(value = "isRecycled", required = false) Boolean isRecycled,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (isRecycled == null) {
            return purchasedItemProcessService.getAllPurchasedItems(date,
                Long.valueOf(userDetails.getId()));
        }

        return purchasedItemProcessService.getRecycledItemsByIsRecycled(
            Long.valueOf(userDetails.getId()), isRecycled);

    }
}
