package com.smu.smuenip.domain.upload;

import com.smu.smuenip.infrastructure.util.Image.ImageUtils;
import com.smu.smuenip.infrastructure.util.cloudFront.CloudFrontApi;
import com.smu.smuenip.infrastructure.util.s3.S3Api;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final S3Api s3Api;
    private final CloudFrontApi cloudFrontApi;

    public String uploadImages(String encodedImage, String dirName) {
        try {
            MultipartFile image = ImageUtils.base64ToMultipartFile(encodedImage);

            s3Api.uploadImageToS3(dirName, image,
                image.getOriginalFilename());

            return cloudFrontApi.generateCloudFrontSignedURL(
                image.getOriginalFilename(),
                cloudFrontApi.setExpiration(3600 * 24 * 7));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("업로드 중 문제가 발생했습니다.", e);
        }
    }
}
