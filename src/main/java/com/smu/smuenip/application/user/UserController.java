package com.smu.smuenip.application.user;

import com.smu.smuenip.Infrastructure.config.redis.CustomUserDetails;
import com.smu.smuenip.application.login.dto.ResponseDto;
import com.smu.smuenip.application.user.dto.UserReceiptUploadRequestDTO;
import com.smu.smuenip.domain.receipt.service.ReceiptProcessingService;
import com.smu.smuenip.enums.meesagesDetail.MessagesSuccess;
import java.awt.print.Pageable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class UserController implements UserControllerSwagger {

    private final ReceiptProcessingService receiptProcessingService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseDto> uploadImage(
        @RequestBody UserReceiptUploadRequestDTO requestDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        receiptProcessingService.processReceipt(requestDTO,
            Long.valueOf(customUserDetails.getId()));

        System.out.println(requestDTO.getLocalDate());
        return new ResponseEntity<>(
            new ResponseDto(true, MessagesSuccess.UPLOAD_SUCCESS.getMessage()),
            HttpStatus.OK);
    }

    @GetMapping("/upload/list")
    public ResponseEntity<ResponseDto> getUploadedItems(
        @PageableDefault Pageable pageable,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return null;
    }
}
