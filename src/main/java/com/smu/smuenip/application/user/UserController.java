package com.smu.smuenip.application.user;

import com.smu.smuenip.Infrastructure.util.naver.ClovaShoppingSearchingApi;
import com.smu.smuenip.application.auth.dto.ResponseDto;
import com.smu.smuenip.domain.image.service.ImageUploadService;
import com.smu.smuenip.enums.meesagesDetail.MessagesSuccess;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final ImageUploadService imageUploadService;
    private final ClovaShoppingSearchingApi api;

    @PostMapping("/upload")
    public ResponseEntity<ResponseDto> uploadFile(@RequestParam("file") MultipartFile file)
        throws IOException {
        // 업로드된 파일 정보 출력
        System.out.println("Uploaded file name: " + file.getOriginalFilename());
        System.out.println("Uploaded file type: " + file.getContentType());
        System.out.println("Uploaded file size: " + file.getSize());

        imageUploadService.uploadImage(file);

        ResponseDto responseDto = new ResponseDto(true,
            MessagesSuccess.UPLOAD_SUCCESS.getMessage());
        // 파일 저장 후 응답
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadBufferedImage(@RequestParam("file") MultipartFile image) {
//        try {
//            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
//            String outputFilePath = "uploads/" + image.getOriginalFilename();
//            File outputFile = new File(outputFilePath);
//
//            ImageIO.write(bufferedImage, getFileExtension(outputFile), outputFile);
//            return new ResponseEntity<>("BufferedImage uploaded and saved.", HttpStatus.OK);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("BufferedImage upload failed.",
//                HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
