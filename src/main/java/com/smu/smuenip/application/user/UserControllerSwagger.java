package com.smu.smuenip.application.user;

import com.smu.smuenip.Infrastructure.config.redis.CustomUserDetails;
import com.smu.smuenip.application.login.dto.ResponseDto;
import com.smu.smuenip.application.user.dto.UserReceiptUploadRequestDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Api(tags = "영수증 업로드 등")
public interface UserControllerSwagger {


    @ApiOperation(value = "영수증을 업로드", notes = "영수증 업로드시 필요한 API")
    ResponseEntity<ResponseDto> uploadImage(
        @RequestBody UserReceiptUploadRequestDTO requestDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails);


}
