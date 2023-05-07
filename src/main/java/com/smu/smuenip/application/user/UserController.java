package com.smu.smuenip.application.user;

import com.smu.smuenip.Infrastructure.config.redis.CustomUserDetails;
import com.smu.smuenip.application.login.dto.ResponseDto;
import com.smu.smuenip.application.user.dto.RecycledImageUploadRequestDto;
import com.smu.smuenip.application.user.dto.UserReceiptResponseDto;
import com.smu.smuenip.application.user.dto.UserSetCommentRequestDto;
import com.smu.smuenip.domain.receipt.service.ReceiptProcessingService;
import com.smu.smuenip.domain.receipt.service.ReceiptService;
import com.smu.smuenip.enums.Messages;
import com.smu.smuenip.enums.meesagesDetail.MessagesSuccess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class UserController implements UserControllerSwagger {

    private final ReceiptProcessingService receiptProcessingService;
    private final ReceiptService receiptService;

    @Override
    @PostMapping("/receipt")
    public ResponseEntity<ResponseDto> uploadImage(
            @RequestBody RecycledImageUploadRequestDto requestDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        receiptProcessingService.processReceipt(requestDTO,
                Long.valueOf(customUserDetails.getId()));

        return new ResponseEntity<>(
                new ResponseDto(true, MessagesSuccess.UPLOAD_SUCCESS.getMessage()),
                HttpStatus.OK);
    }

    @Override
    @GetMapping("/receipt/list")
    public List<UserReceiptResponseDto> getUploadedItems(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(sort = "id") Pageable pageable) {

        return receiptService.findReceiptsByDate(date, Long.valueOf(userDetails.getId()), pageable);
    }

    @PutMapping("/receipt")
    public ResponseEntity<Messages> setComment(@RequestBody UserSetCommentRequestDto requestDto,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        receiptService.setComment(requestDto, Long.valueOf(userDetails.getId()));
        return new ResponseEntity<>(MessagesSuccess.COMMENT_SUCCESS, HttpStatus.OK);
    }
}
