package com.smu.smuenip.infrastructure.util.Image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageUtils {

    public static MultipartFile resizeImage(MultipartFile originalImage) {
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

    public static MultipartFile base64ToMultipartFile(String base64) {
        String data = base64.split(",")[1];
        byte[] decoded = Base64.getMimeDecoder().decode(data);
        String contentType = extractContentType(base64);
        String generatedImageName = generateUUID();
        return new MockMultipartFile(generatedImageName,
                generatedImageName + "." + contentType, contentType, decoded);
    }

    private static String extractContentType(String base64) {
        Pattern pattern = Pattern.compile("data:image/(.+?);base64");
        Matcher matcher = pattern.matcher(base64);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

}
