package com.smu.smuenip.application.user;

import com.smu.smuenip.AmazonS3MockConfiguration;
import com.smu.smuenip.Infrastructure.config.security.BCryptPasswordEncoderConfig;
import com.smu.smuenip.Infrastructure.util.naver.search.ClovaShoppingSearchingAPI;
import com.smu.smuenip.Infrastructure.util.s3.S3API;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.model.UserAuth;
import com.smu.smuenip.domain.user.repository.UserAuthRepository;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.Provider;
import com.smu.smuenip.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@Import({AmazonS3MockConfiguration.class, BCryptPasswordEncoderConfig.class})
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {


    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAuthRepository userAuthRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    S3API s3API;

    @MockBean
    ClovaShoppingSearchingAPI clovaShoppingSearchingAPI;

    @MockBean
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        User user = User.builder()
            .loginId("test1234")
            .email("test1234@gmail.com")
            .score(111)
            .role(Role.ROLE_USER)
            .build();

        userRepository.save(user);

        UserAuth userAuth = UserAuth.builder()
            .user(user)
            .password(passwordEncoder.encode("test1234"))
            .provider(Provider.LOCAL)
            .build();

        userAuthRepository.save(userAuth);
    }
//
//    @Test
//    void uploadImage() {
//        String token = createToken();
//
//        Path imagePath = Paths.get("src/test/resources/image/IMG_20230327_135556.JPG");
//        try (InputStream imageStream = Files.newInputStream(imagePath)) {
//            MockMultipartFile imageFile = new MockMultipartFile("file", "test_image.jpg",
//                MediaType.IMAGE_JPEG_VALUE, imageStream);
//
//            mockMvc.perform(multipart("/upload")
//                .file(imageFile)
//                .header("Authorization", "Bearer " + token)
//            ).andExpect(status().isOk());
//
//            List<Item> itemList = new ArrayList<>();
//            Item item = new Item();
//            item.setCategory1("커피1");
//            item.setCategory2("커피2");
//            item.setCategory3("커피3");
//            item.setCategory4("커피4");
//
//            itemList.add(item);
//            Mockito.when(clovaShoppingSearchingAPI.callShoppingApi(Mockito.any(String.class)))
//                .thenReturn(new ItemDTO(
//                    null, 0, 0, 0, itemList
//                ));
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

}
