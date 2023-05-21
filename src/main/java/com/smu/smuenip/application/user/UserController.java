package com.smu.smuenip.application.user;

import com.smu.smuenip.application.login.dto.ResponseDto;
import com.smu.smuenip.application.user.dto.UserImageUploadRequestDto;
import com.smu.smuenip.application.user.dto.UserReceiptResponseDto;
import com.smu.smuenip.application.user.dto.UserSetCommentRequestDto;
import com.smu.smuenip.domain.receipt.service.ReceiptService;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesSuccess;
import com.smu.smuenip.infrastructure.config.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class UserController implements UserControllerSwagger {

    private final ReceiptService receiptService;

    @Override
    @PostMapping("/receipt")
    public ResponseDto<Void> uploadImage(
            @RequestBody UserImageUploadRequestDto requestDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(requestDTO.getDate(), formatter);
        receiptService.uploadReceipt(requestDTO.getImage(), date,
                Long.valueOf(customUserDetails.getId()));

        return new ResponseDto<>(null, true, MessagesSuccess.UPLOAD_SUCCESS.getMessage());
    }


    @Override
    @GetMapping("/receipt/list")
    public List<UserReceiptResponseDto> getUploadedItems(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(sort = "id") Pageable pageable) {

        return receiptService.findReceiptsByDate(date, Long.valueOf(userDetails.getId()), pageable);
    }

    @Override
    @PutMapping("/receipt")
    public ResponseDto<Void> setComment(@RequestBody UserSetCommentRequestDto requestDto,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        receiptService.setComment(requestDto, Long.valueOf(userDetails.getId()));
        return new ResponseDto<>(null, true, MessagesSuccess.COMMENT_SUCCESS.getMessage());
    }
}
