package com.smu.smuenip.user.application;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.smuenip.application.auth.dto.UserRequestDto;
import com.smu.smuenip.domain.user.model.Role;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.RoleRepository;
import com.smu.smuenip.domain.user.repository.UserAuthRepository;
import com.smu.smuenip.domain.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
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
    private RoleRepository roleRepository;

    @BeforeAll()
    void init() {
        Role roleUser = Role.builder()
            .id(1L)
            .name("ROLE_USER")
            .build();
        Role roleAdmin = Role.builder()
            .id(2L)
            .name("ROLE_ADMIN")
            .build();

        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);
    }

    @Test
    void signUpTest() throws Exception {
        //given
        String expectedContentType = "application/json";
        UserRequestDto userRequestDto = UserRequestDto.builder()
            .loginId("test12a")
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
        String actualContentType = resultFail.getResponse().getHeader("Content-Type");
        Optional<User> userOptional = userRepository.findUserByLoginId(userRequestDto.getLoginId());

        // then
        Assertions.assertThat(actualOK).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(actualBadRequest).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(actualContentType).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertUserAndAuthExist(userOptional);
    }

    @Test
    void loginTest() {
        //given
        Jwts.builder();
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
