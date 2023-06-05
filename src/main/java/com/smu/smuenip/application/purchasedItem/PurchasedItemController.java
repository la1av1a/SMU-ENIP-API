package com.smu.smuenip.application.purchasedItem;

import com.smu.smuenip.application.purchasedItem.dto.PurchasedItemResponseDto;
import com.smu.smuenip.domain.purchasedItem.service.PurchasedItemService;
import com.smu.smuenip.infrastructure.config.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/item")
@RestController
@RequiredArgsConstructor
public class PurchasedItemController implements PurchasedItemControllerSwagger {

    private final PurchasedItemService purchasedItemService;

    @Override
    @GetMapping("/list")
    public List<PurchasedItemResponseDto> getPurchasedItems(
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault Pageable pageable) {
        return purchasedItemService.getPurchasedItems(date, Long.valueOf(userDetails.getId()),
                pageable);
    }
}
