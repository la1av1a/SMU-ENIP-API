package com.smu.smuenip.user.domain.repository;

import com.smu.smuenip.infra.config.security.BCryptPasswordEncoderConfig;
import com.smu.smuenip.user.domain.enums.Provider;
import com.smu.smuenip.user.domain.model.Users;
import com.smu.smuenip.user.domain.model.UsersAuth;
import java.util.NoSuchElementException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

@Import(BCryptPasswordEncoderConfig.class)
@DataJpaTest
class UsersAuthRepositoryTest {

    @Autowired
    UsersAuthRepository usersAuthRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    String userId = "test1234";
    String password = "password";
    String email = "test1234@example.com";
    String phoneNumber = "01000000000";


    @BeforeEach
    void setup() {

        Users users = Users.builder()
            .userId(userId)
            .email(email)
            .build();

        UsersAuth usersAuth = UsersAuth.builder()
            .user(users)
            .password(passwordEncoder.encode(password))
            .provider(Provider.LOCAL)
            .phoneNumber(phoneNumber)
            .build();
        userRepository.save(users);
        usersAuthRepository.save(usersAuth);
    }

    @Test
    void saveTest() {
        //when
        Users users = userRepository.findUserByUserId(userId).orElseThrow(
            NoSuchElementException::new);

        UsersAuth usersAuth = usersAuthRepository.findById(1L)
            .orElseThrow(NoSuchElementException::new);

        //then
        Assertions.assertThat(users.getEmail()).isEqualTo(email);
        Assertions.assertThat(usersAuth.getUser().getUserId()).isEqualTo(userId);
        Assertions.assertThat(passwordEncoder.matches(password, usersAuth.getPassword())).isTrue();
    }
}
