package com.smu.smuenip.user.application;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.smuenip.application.user.dto.UserRequestDto;
import com.smu.smuenip.enums.Messages;
import com.smu.smuenip.enums.meesagesDetail.MessagesFail;
import com.smu.smuenip.enums.meesagesDetail.MessagesSuccess;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.domain.user.repository.UserAuthRepository;
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
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAuthRepository userAuthRepository;

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
        Optional<User> userOptional = userRepository.findUserByUserId(userRequestDto.getUserId());

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
