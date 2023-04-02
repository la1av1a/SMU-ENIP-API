package com.smu.smuenip.Infrastructure.util.Image;

import com.smu.smuenip.Infrastructure.config.exception.UnExpectedErrorException;
import com.smu.smuenip.enums.meesagesDetail.MessagesFail;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    public static void deleteLocalSavedImage(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String resizeImage(MultipartFile file) throws UnExpectedErrorException {
        try {
            MultipartFile resizedImage = resizeImage(file.getOriginalFilename(),
                file.getContentType(), file, 768);
            log.info("size : {}", file.getInputStream());
            return saveImageInLocal(resizedImage);
        } catch (IOException e) {
            throw new UnExpectedErrorException(MessagesFail.UNEXPECTED_ERROR.getMessage());
        }
    }

    private void test(MultipartFile image, BufferedImage resized) throws IOException {

        String outputFilePath = "uploads/" + image.getOriginalFilename();
        File outputFile = new File(outputFilePath);

        ImageIO.write(resized, getFileExtension(outputFile), outputFile);
    }

    private static String saveImageInLocal(MultipartFile image) throws IOException {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(image.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("이미지를 읽는 도중 오류가 발생했습니다.", e);
        }

        if (image == null) {
            throw new RuntimeException("이미지 파일이 올바르지 않거나 지원되지 않는 형식입니다.");
        }
        String outputFilePath = "uploads/" + image.getOriginalFilename();
        log.info(outputFilePath);
        File outputFile = new File(outputFilePath);

        ImageIO.write(bufferedImage, getFileExtension(outputFile), outputFile);

        return outputFilePath;
    }

    private BufferedImage multipartFileToBufferedImage(MultipartFile file)
        throws IOException {

        return ImageIO.read(file.getInputStream());
    }

    public static MultipartFile resizeImage(String fileName, String fileFormatName,
        MultipartFile originalImage, int targetWidth) {
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


    private static String getFileExtension(File file) {
        String fileName = file.getName();
        int index = fileName.lastIndexOf('.');
        return index > 0 ? fileName.substring(index + 1) : "";
    }

}
