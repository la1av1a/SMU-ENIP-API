package com.smu.smuenip.domain.externalAPI;

import com.smu.smuenip.Infrastructure.util.naver.PurchasedItemVO;
import com.smu.smuenip.Infrastructure.util.naver.ocr.ClovaOCRAPI;
import com.smu.smuenip.Infrastructure.util.naver.ocr.ocrResult.OcrResultDto2;
import com.smu.smuenip.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class APIService {

    private final ImageService imageService;
    private final ClovaOCRAPI clovaOCRAPI;

    public List<PurchasedItemVO> callAPI(String localFilePath) {
        OcrResultDto2 ocrResultDTO = clovaOCRAPI.callOcrApi(localFilePath);
        return imageService.extractPurchasedInfo(
                ocrResultDTO);
    }

}
