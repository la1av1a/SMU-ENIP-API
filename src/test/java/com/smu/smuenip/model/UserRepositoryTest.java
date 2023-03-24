package com.smu.smuenip.model;

import com.smu.smuenip.Infrastructure.config.security.BCryptPasswordEncoderConfig;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import java.util.NoSuchElementException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

@Import(BCryptPasswordEncoderConfig.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("유저가 잘 저장되는지 확인")
    void userSaveTest() {
        //given

        String userId = "test1234";

        User user = User.builder()
            .userId(userId)
            .email("test1234@gmail.com")

            .build();

        //when
        userRepository.save(user);

        User expect = userRepository.findUserByUserId(userId)
            .orElseThrow(NoSuchElementException::new);

        //then
        Assertions.assertThat(expect.getUserId()).isEqualTo(userId);
    }
}
