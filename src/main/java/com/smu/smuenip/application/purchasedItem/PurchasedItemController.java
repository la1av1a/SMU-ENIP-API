package com.smu.smuenip.application.purchasedItem;

import com.smu.smuenip.Infrastructure.config.redis.CustomUserDetails;
import com.smu.smuenip.application.purchasedItem.dto.PurchasedItemResponseDto;
import com.smu.smuenip.domain.PurchasedItem.service.PurchasedItemService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    private final PurchasedItemService purchasedItemService;

    @Override
    @GetMapping("/list")
    public List<PurchasedItemResponseDto> getPurchasedItems(
        @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PageableDefault Pageable pageable) {
        return purchasedItemService.getPurchasedItems(date, Long.valueOf(userDetails.getId()),
            pageable);
    }
}
