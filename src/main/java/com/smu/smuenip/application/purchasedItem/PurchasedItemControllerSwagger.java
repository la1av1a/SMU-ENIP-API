package com.smu.smuenip.application.purchasedItem;

import com.smu.smuenip.application.purchasedItem.dto.PurchasedItemResponseDto;
import com.smu.smuenip.infrastructure.config.CustomUserDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;

@Api(tags = "구매한 상품들 관련")
public interface PurchasedItemControllerSwagger {

    @ApiOperation(value = "구매목록 요청")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "date", dataType = "datetime", paramType = "query",
            value = "yyyy-MM-dd"),
        @ApiImplicitParam(name = "Authorization", dataType = "String", paramType = "header",
            value = "Bearer <token>"),
    })
    public List<PurchasedItemResponseDto> getPurchasedItems(
        @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PageableDefault Pageable pageable);
}
