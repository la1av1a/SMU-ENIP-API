package com.smu.smuenip.application.admin;

import com.smu.smuenip.application.login.dto.ResponseDto;
import com.smu.smuenip.domain.approve.ApproveProcessService;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesSuccess;
import com.smu.smuenip.infrastructure.config.CustomUserDetails;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class ApproveController {

    private final ApproveProcessService approveProcessService;

    @PostMapping("/approve")
    public ResponseEntity<ResponseDto<?>> approveRecycledImage(
        @RequestBody @Valid ApproveRequestDto approveRequestDto,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        
        approveProcessService.processRecycledImage(approveRequestDto.getRecycledImageId(),
            Long.parseLong(customUserDetails.getId()), approveRequestDto.isApprove());

        if (approveRequestDto.isApprove()) {
            return new ResponseEntity<>(
                new ResponseDto<>(null, MessagesSuccess.APPROVE_SUCCESS.getMessage()),
                HttpStatus.OK);
        }

        return new ResponseEntity<>(
            new ResponseDto<>(null, MessagesSuccess.REJECT_SUCCESS.getMessage()),
            HttpStatus.OK);
    }
}
