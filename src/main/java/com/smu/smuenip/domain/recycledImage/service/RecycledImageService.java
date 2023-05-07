package com.smu.smuenip.domain.recycledImage.service;

import com.smu.smuenip.Infrastructure.util.Image.ImageUtils;
import com.smu.smuenip.application.user.dto.RecycledImageUploadRequestDto;
import com.smu.smuenip.domain.image.service.ImageService;
import com.smu.smuenip.domain.recycledImage.entity.RecycledImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecycledImageService {

    private final RecycledImageRepository recycledImageRepository;
    private final ImageUtils imageUtils;
    private final ImageService imageService;

    public void uploadRecycledImage(RecycledImageUploadRequestDto requestDto, Long userId) {

        // 분리수거 사진 업로드
        MultipartFile multipartFile = imageService.base64ToMultipartFile(requestDto.getImage());
        String imagePath = imageService.uploadImageToS3(multipartFile, UUID.randomUUID().toString());
        //    분리수거 미션을 관리하는 테이블에 저장
        //        - 사진은 자신이 구매한 품목에 대한 것이여야함
        
        //uploaded_date
    }
}
