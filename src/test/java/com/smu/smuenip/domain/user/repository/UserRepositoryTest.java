package com.smu.smuenip.domain.user.repository;

import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.enums.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    User savedUser;

    @BeforeEach
    void setUp() {
        savedUser = userRepository.save(User.builder()
                .loginId("test12345")
                .role(Role.ROLE_USER)
                .email("test1234@gmail.com")
                .score(0)
                .build());
    }

    @Test
    void findUserByLoginId() {

        //when
        userRepository.findUserByLoginId(savedUser.getLoginId()).ifPresent(user -> {
            //then
            Assertions.assertThat(user.getLoginId()).isEqualTo("test12345");
        });
    }

    @Test
    void findUserByUserId() {
        //when
        userRepository.findUserByUserId(savedUser.getUserId()).ifPresent(user -> {
            //then
            Assertions.assertThat(user.getUserId()).isEqualTo(savedUser.getUserId());
        });
    }

    @Test
    void existsUsersByLoginId() {
        //when
        boolean exists = userRepository.existsUsersByLoginId(savedUser.getLoginId());

        //then
        Assertions.assertThat(exists).isTrue();
    }
}