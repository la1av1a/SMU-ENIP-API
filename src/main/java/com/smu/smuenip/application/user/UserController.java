package com.smu.smuenip.application.user;

import com.smu.smuenip.domain.amazon.S3ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final S3ImageUploadService s3ImageUploadService;

//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file)
//        throws IOException {
//        // 업로드된 파일 정보 출력
//        System.out.println("Uploaded file name: " + file.getOriginalFilename());
//        System.out.println("Uploaded file type: " + file.getContentType());
//        System.out.println("Uploaded file size: " + file.getSize());
//
//        // 파일을 서버에 저장
//        Path filePath = Paths.get("uploads/" + file.getOriginalFilename());
//        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//        String res = s3ImageUploadService.upload(filePath.toString(), file.getOriginalFilename());
//        System.out.println(res);
//        // 파일 저장 후 응답
//        return new ResponseEntity<>(res, org.springframework.http.HttpStatus.OK);
//
//    }

}
