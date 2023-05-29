package com.smu.smuenip.application.recycle;

import com.smu.smuenip.application.login.dto.ResponseDto;
import com.smu.smuenip.application.purchasedItem.dto.RecycledImageUploadRequestDto;
import com.smu.smuenip.domain.recycledImage.RecycledImageService;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesSuccess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/item/image")
@RequiredArgsConstructor
public class RecycleController {

    private final RecycledImageService recycledImageService;

    @PostMapping("")
    public ResponseEntity<ResponseDto<Void>> recycledImageUpload(
            @RequestBody RecycledImageUploadRequestDto requestDto) {

        recycledImageService.RecycledImageUpload(requestDto);

        return new ResponseEntity<>(new ResponseDto<>(null, MessagesSuccess.UPLOAD_SUCCESS.getMessage()), HttpStatus.OK);
    }
}
