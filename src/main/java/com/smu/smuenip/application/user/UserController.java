package com.smu.smuenip.application.user;

import com.smu.smuenip.Infrastructure.config.redis.TokenInfo;
import com.smu.smuenip.application.login.dto.ResponseDto;
import com.smu.smuenip.domain.receipt.service.ReceiptProcessingService;
import com.smu.smuenip.enums.meesagesDetail.MessagesSuccess;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final ReceiptProcessingService receiptProcessingService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseDto> uploadImage(@RequestParam("file") MultipartFile image,
        @AuthenticationPrincipal TokenInfo tokenInfo) {
        receiptProcessingService.processReceipt(image, tokenInfo);
        return new ResponseEntity<>(
            new ResponseDto(true, MessagesSuccess.UPLOAD_SUCCESS.getMessage()),
            HttpStatus.OK);
    }
}
