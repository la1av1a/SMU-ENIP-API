package com.smu.smuenip.user.application;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.smuenip.Infrastructure.config.security.BCryptPasswordEncoderConfig;
import com.smu.smuenip.application.login.dto.LoginRequestDto;
import com.smu.smuenip.application.login.dto.UserRequestDto;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.model.UserAuth;
import com.smu.smuenip.domain.user.repository.UserAuthRepository;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.Provider;
import com.smu.smuenip.enums.Role;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(BCryptPasswordEncoderConfig.class)
@TestInstance(Lifecycle.PER_CLASS)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @Test
    void signUpTest() throws Exception {
        //given
        UserRequestDto userRequestDto = UserRequestDto.builder()
            .loginId("c")
            .email("test12a@example.com")
            .password("password")
            .build();

        //when
        MvcResult resultSuccess = mockMvc.perform(post("/user/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToString(userRequestDto))
            )
            .andExpect(status().isOk())
            .andReturn();

        //중복 회원가입
        MvcResult resultFail = mockMvc.perform(post("/user/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToString(userRequestDto)))
            .andExpect(status().isBadRequest())
            .andReturn();

        int actualOK = resultSuccess.getResponse().getStatus();
        int actualBadRequest = resultFail.getResponse().getStatus();
        Optional<User> userOptional = userRepository.findUserByLoginId(userRequestDto.getLoginId());

        // then
        Assertions.assertThat(actualOK).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(actualBadRequest).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertUserAndAuthExist(userOptional);
    }

    @Test
    void loginTest() throws Exception {
        LoginRequestDto loginRequestDto =
            new LoginRequestDto("test1234", "test1234");

        MvcResult mvcResult = mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToString(loginRequestDto)))
            .andExpect(status().isOk())
            .andReturn();

        Assertions.assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
    }

    private String objectToString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }


    private void assertUserAndAuthExist(Optional<User> userOptional) {
        userOptional.map(user -> userAuthRepository.findUsersAuthsByUser(user))
            .ifPresentOrElse(
                usersAuthOptional -> usersAuthOptional.ifPresentOrElse(
                    usersAuth -> assertTrue(true),
                    () -> fail("UsersAuth not found")),
                () -> fail("User not found")
            );
    }
}
