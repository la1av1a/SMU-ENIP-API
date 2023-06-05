package com.smu.smuenip.application.recycle;

import com.smu.smuenip.application.login.dto.ResponseDto;
import com.smu.smuenip.application.purchasedItem.dto.RecycledImageUploadRequestDto;
import com.smu.smuenip.infrastructure.config.CustomUserDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Api(tags = "재활용된 이미지 API")
public interface RecycleControllerSwagger {

    @ApiOperation(value = "재활용된 이미지 업로드", notes = "재활용된 이미지를 업로드한다.")
    ResponseEntity<ResponseDto<Void>> recycledImageUpload(
            @RequestBody RecycledImageUploadRequestDto requestDto);


    //date를 안 넣으면 모든 날짜 리턴, date를 넣으면 해당 날짜 리턴을 apiOperation 어노테이션으로
    @ApiOperation(value = "재활용된 이미지 리스트 조회", notes = "재활용된 이미지 리스트를 조회한다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "date", dataType = "datetime", paramType = "query",
                    value = "yyyy-MM-dd", example = "2021-01-01"),
            @ApiImplicitParam(name = "isRecycled", dataType = "boolean", paramType = "query",
                    value = "true or false"),
    })
    ResponseEntity<ResponseDto<?>> getRecycledItems(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "date", required = false) LocalDate date,
            @RequestParam(value = "isRecycled", required = false) Boolean isRecycled);
}
