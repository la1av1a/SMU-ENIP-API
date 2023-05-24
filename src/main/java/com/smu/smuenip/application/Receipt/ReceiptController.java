package com.smu.smuenip.application.Receipt;

import com.smu.smuenip.application.Receipt.dto.ReceiptImageUploadRequestDto;
import com.smu.smuenip.application.Receipt.dto.ReceiptSetCommentRequestDto;
import com.smu.smuenip.application.Receipt.dto.UserReceiptResponseDto;
import com.smu.smuenip.application.login.dto.ResponseDto;
import com.smu.smuenip.domain.receipt.service.ReceiptService;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesSuccess;
import com.smu.smuenip.infrastructure.config.CustomUserDetails;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class ReceiptController implements ReceiptControllerSwagger {

    private final ReceiptService receiptService;

    @Override
    @PostMapping("/receipt")
    public ResponseEntity<ResponseDto<Void>> uploadImage(
            @RequestBody ReceiptImageUploadRequestDto requestDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(requestDTO.getDate(), formatter);
        receiptService.uploadReceipt(requestDTO.getImage(), date,
                Long.valueOf(customUserDetails.getId()));

        return new ResponseEntity<>(new ResponseDto<>(null, MessagesSuccess.UPLOAD_SUCCESS.getMessage()), HttpStatus.OK);
    }


    @Override
    @GetMapping("/receipt/list")
    public List<UserReceiptResponseDto> getUploadedItems(
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(sort = "id") Pageable pageable) {

        return receiptService.findReceiptsByDate(date, Long.valueOf(userDetails.getId()), pageable);
    }

    @Override
    @PatchMapping("/receipt")
    public ResponseEntity<ResponseDto<Void>> setComment(@RequestBody ReceiptSetCommentRequestDto requestDto,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        receiptService.setComment(requestDto, Long.valueOf(userDetails.getId()));
        return new ResponseEntity<>(new ResponseDto<>(null, MessagesSuccess.COMMENT_SUCCESS.getMessage()), HttpStatus.OK);
    }
}
