package com.smu.smuenip.Infrastructure.util.Image;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageUtils {

    public MultipartFile resizeImage(MultipartFile originalImage) {
        int targetWidth = 300;
        String fileName = originalImage.getOriginalFilename();
        try {
            // MultipartFile -> BufferedImage Convert
            BufferedImage image = ImageIO.read(originalImage.getInputStream());
            int originWidth = image.getWidth();
            int originHeight = image.getHeight();

            // origin 이미지가 resizing될 사이즈보다 작을 경우 resizing 작업 안 함
            if (originWidth < targetWidth) {
                return originalImage;
            }

            // Calculate new height with aspect ratio
            int targetHeight = (targetWidth * originHeight) / originWidth;

            // Create a new BufferedImage with the target dimensions
            BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight,
                BufferedImage.TYPE_INT_RGB);

            // Draw the original image onto the resized image using Graphics2D
            Graphics2D g = resizedImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(image, 0, 0, targetWidth, targetHeight, null);
            g.dispose();

            // Get the file extension to determine the image format
            String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);

            // Convert the resized BufferedImage to MultipartFile
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, fileExtension, baos);
            baos.flush();

            return new MockMultipartFile(fileName, originalImage.getOriginalFilename(),
                originalImage.getContentType(), baos.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("파일 리사이즈에 실패했습니다.", e);
        }
    }


}
