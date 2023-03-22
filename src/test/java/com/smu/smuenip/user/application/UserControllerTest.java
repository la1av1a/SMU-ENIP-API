package com.smu.smuenip.user.application;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.smuenip.user.application.dto.UserRequestDto;
import com.smu.smuenip.user.application.enums.Messages;
import com.smu.smuenip.user.application.enums.MessagesFail;
import com.smu.smuenip.user.application.enums.MessagesSuccess;
import com.smu.smuenip.user.domain.model.Users;
import com.smu.smuenip.user.domain.repository.UserRepository;
import com.smu.smuenip.user.domain.repository.UsersAuthRepository;
import io.jsonwebtoken.Jwts;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UsersAuthRepository usersAuthRepository;

    @Test
    void signUpTest() throws Exception {
        //given
        UserRequestDto userRequestDto = UserRequestDto.builder()
            .userId("test1234")
            .email("test1234@example.com")
            .password("password")
            .phoneNumber("010-1234-5678")
            .build();

        //when
        MvcResult resultSuccess = mockMvc.perform(post("/user/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)))
            .andExpect(status().isOk())
            .andReturn();

        //중복 회원가입
        MvcResult resultFail = mockMvc.perform(post("/user/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)))
            .andExpect(status().isConflict())
            .andReturn();

        String responseString = resultSuccess.getResponse().getContentAsString();
        String responseFailString = resultFail.getResponse().getContentAsString();
        Messages successMessage = objectMapper.readValue(responseString, MessagesSuccess.class);
        Messages failMessage = objectMapper.readValue(responseFailString, MessagesFail.class);
        Optional<Users> userOptional = userRepository.findUserByUserId(userRequestDto.getUserId());

        // then

        Assertions.assertThat(successMessage).isEqualTo(MessagesSuccess.SIGNUP_SUCCESS);
        Assertions.assertThat(failMessage).isEqualTo(MessagesFail.USER_EXISTS);

        assertUserAndAuthExist(userOptional);
    }

    @Test
    void loginTest() {
        //given
        Jwts.builder();
    }

    private void assertUserAndAuthExist(Optional<Users> userOptional) {
        userOptional.map(user -> usersAuthRepository.findUsersAuthsByUser(user))
            .ifPresentOrElse(
                usersAuthOptional -> usersAuthOptional.ifPresentOrElse(
                    usersAuth -> assertTrue(true),
                    () -> fail("UsersAuth not found")),
                () -> fail("User not found")
            );
    }
}
