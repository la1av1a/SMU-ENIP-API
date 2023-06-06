package com.smu.smuenip.application.recycle;

import com.smu.smuenip.application.login.dto.ResponseDto;
import com.smu.smuenip.application.purchasedItem.dto.RecycledImageUploadRequestDto;
import com.smu.smuenip.application.recycle.dto.RecycledImageResponseDto;
import com.smu.smuenip.domain.recycledImage.service.RecycledImageProcessService;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesSuccess;
import com.smu.smuenip.infrastructure.config.CustomUserDetails;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/recycle/image")
@RequiredArgsConstructor
public class RecycleController implements RecycleControllerSwagger {

    private final RecycledImageProcessService recycledImageProcessService;

    @PostMapping("")
    public ResponseEntity<ResponseDto<Void>> recycledImageUpload(
        @RequestBody RecycledImageUploadRequestDto requestDto) {
        recycledImageProcessService.RecycledImageUpload(requestDto);

        return new ResponseEntity<>(
            new ResponseDto<>(null, MessagesSuccess.UPLOAD_SUCCESS.getMessage()), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDto<?>> getRecycledItems(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(value = "date", required = false) LocalDate date,
        @RequestParam(value = "isApproved", required = false) Boolean isApproved) {

        List<RecycledImageResponseDto> recycledImageResponseDtoList = recycledImageProcessService.getRecycledItems(
            Long.parseLong(userDetails.getId()), userDetails.getRole(), date, isApproved);

        return new ResponseEntity<>(new ResponseDto<>(recycledImageResponseDtoList,
            MessagesSuccess.GET_SUCCESS.getMessage()), HttpStatus.OK);
    }
}
