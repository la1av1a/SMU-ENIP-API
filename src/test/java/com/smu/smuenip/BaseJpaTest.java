package com.smu.smuenip;


import com.smu.smuenip.Infrastructure.config.security.BCryptPasswordEncoderConfig;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.model.UserAuth;
import com.smu.smuenip.domain.user.repository.UserAuthRepository;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.Role;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

@Import(BCryptPasswordEncoderConfig.class)
@DataJpaTest
public class BaseJpaTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAuthRepository userAuthRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @BeforeEach
    public void setUp() {
        User user = User.builder()
            .loginId("test1234")
            .email("test1234@gmail.com")
            .score(0)
            .role(Role.ROLE_USER)
            .build();

        UserAuth userAuth = UserAuth.builder()
            .user(user)
            .password(passwordEncoder.encode("test1234"))
            .build();
        userRepository.save(user);
        userAuthRepository.save(userAuth);
    }

    @Test
    void test() {
        String pw = "test1234";

        Optional<UserAuth> userAuth = userAuthRepository.findById(1L);
        Assertions.assertThat(userAuth.isPresent()).isTrue();
        Assertions.assertThat(passwordEncoder.matches(pw, userAuth.get().getPassword())).isTrue();
    }
}
