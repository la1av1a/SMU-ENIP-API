package com.smu.smuenip.domain.externalAPI;

import com.smu.smuenip.Infrastructure.util.naver.PurchasedItemVO;
import com.smu.smuenip.Infrastructure.util.naver.ocr.ClovaOCRAPI;
import com.smu.smuenip.Infrastructure.util.naver.ocr.ocrResult.OcrResultDTO;
import com.smu.smuenip.domain.image.service.ImageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class APIService {

    private final ImageService imageService;
    private final ClovaOCRAPI clovaOCRAPI;

    public List<PurchasedItemVO> callAPI(String localFilePath) {
        OcrResultDTO ocrResultDTO = clovaOCRAPI.callOcrApi(localFilePath);
        return imageService.extractPurchasedInfo(
            ocrResultDTO);
    }

}
