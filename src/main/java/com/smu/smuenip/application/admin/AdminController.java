package com.smu.smuenip.application.admin;

import com.smu.smuenip.application.recycle.dto.RecycledImageResponseDto;
import com.smu.smuenip.domain.recycledImage.service.RecycledImageProcessService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final RecycledImageProcessService recycledImageProcessService;

    @GetMapping("/item/list")
    public List<RecycledImageResponseDto> getRecycledImageList(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @RequestParam(value = "isRecycled", required = false) Boolean isRecycled) {

        return recycledImageProcessService.getRecycledItems(Long.parseLong(userDetails.getId()),
            userDetails.getRole(), date, isRecycled);
    }
}
