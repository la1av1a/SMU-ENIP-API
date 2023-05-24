package com.smu.smuenip.application.user;

import com.smu.smuenip.JwtTokenUtil;
import com.smu.smuenip.application.Receipt.dto.ReceiptImageUploadRequestDto;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.model.UserAuth;
import com.smu.smuenip.domain.user.repository.UserAuthRepository;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.Provider;
import com.smu.smuenip.enums.Role;
import com.smu.smuenip.infrastructure.config.security.BCryptPasswordEncoderConfig;
import com.smu.smuenip.infrastructure.util.naver.ItemDto;
import com.smu.smuenip.infrastructure.util.naver.ocr.ClovaOcrApi;
import com.smu.smuenip.infrastructure.util.naver.ocr.OcrRequestDto;
import com.smu.smuenip.infrastructure.util.naver.ocr.dto.OcrResponseDto;
import com.smu.smuenip.infrastructure.util.naver.search.ClovaShoppingSearchingAPI;
import com.smu.smuenip.infrastructure.util.s3.S3Api;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(BCryptPasswordEncoderConfig.class)
@Transactional
class ReceiptControllerTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserAuthRepository userAuthRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    ClovaOcrApi clovaOcrApi;
    @MockBean
    S3Api s3Api;
    @MockBean
    ClovaShoppingSearchingAPI clovaShoppingSearchingApi;


    @InjectMocks
    ObjectMapper objectMapper;
    User savedUser;
    UserAuth savedUserAuth;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        String loginId = "test1234";
        String email = "test1234@gmail.com";
        String password = passwordEncoder.encode("test1234");
        User user = new User(loginId, email, Role.ROLE_USER, 0);
        savedUser = userRepository.save(user);

        UserAuth userAuth = new UserAuth(user, Provider.LOCAL, password, loginId);
        savedUserAuth = userAuthRepository.save(userAuth);
    }

    @Test
    void uploadImage() throws Exception {

        String token = JwtTokenUtil.createToken(savedUser.getUserId(), savedUser.getEmail(), savedUserAuth.getProviderId(), savedUser.getRole());

        File jsonFile = new File("src/test/resources/json/ocr.json");
        OcrResponseDto ocrResponseDto = objectMapper.readValue(jsonFile, OcrResponseDto.class);

        File searchDto = new File("src/test/resources/json/searching.json");
        ItemDto itemDto = objectMapper.readValue(searchDto, ItemDto.class);

        //when
        Mockito.when(clovaOcrApi.callNaverOcr(Mockito.any(OcrRequestDto.Images.class))).thenReturn(ocrResponseDto);
        Mockito.when(clovaShoppingSearchingApi.callShoppingApi(Mockito.any(String.class))).thenReturn(itemDto);
        Mockito.when(s3Api.uploadImageToS3(Mockito.any(MultipartFile.class), Mockito.any(String.class))).thenReturn("ss.com");

        File imageFile = new File("src/test/resources/image/IMG_20230327_135556.JPG");
        String mimeType = Files.probeContentType(imageFile.toPath());
        byte[] fileContent = Files.readAllBytes(imageFile.toPath());
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        String dataUrl = "data:" + mimeType + ";base64," + encodedString;

        ReceiptImageUploadRequestDto requestDto = new ReceiptImageUploadRequestDto();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDate.parse("2023-04-01", dateTimeFormatter).toString();

        requestDto.setDate(date);
        requestDto.setImage(dataUrl);

        mockMvc.perform(post("/receipt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

}