package com.smu.smuenip.user.domain.repository;

import com.smu.smuenip.Infrastructure.config.security.BCryptPasswordEncoderConfig;
import com.smu.smuenip.domain.user.repository.UserAuthRepository;
import com.smu.smuenip.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

@Import(BCryptPasswordEncoderConfig.class)
@DataJpaTest
class UserAuthRepositoryTest {

    @Autowired
    UserAuthRepository userAuthRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    String userId = "test1234";
    String password = "password";
    String email = "test1234@example.com";
    String phoneNumber = "01000000000";

//
//    @BeforeEach
//    void setup() {
//
//        User user = User.builder()
//            .userId(userId)
//            .email(email)
//            .build();
//
//        UserAuth userAuth = UserAuth.builder()
//            .user(user)
//            .password(passwordEncoder.encode(password))
//            .provider(Provider.LOCAL)
//            .phoneNumber(phoneNumber)
//            .build();
//        userRepository.save(user);
//        userAuthRepository.save(userAuth);
//    }
//
//    @Test
//    void saveTest() {
//        //when
//        User user = userRepository.findUserByUserId(userId).orElseThrow(
//            NoSuchElementException::new);
//
//        UserAuth userAuth = userAuthRepository.findById(1L)
//            .orElseThrow(NoSuchElementException::new);
//
//        //then
//        Assertions.assertThat(user.getEmail()).isEqualTo(email);
//        Assertions.assertThat(userAuth.getUser().getLoginId()).isEqualTo(userId);
//        Assertions.assertThat(passwordEncoder.matches(password, userAuth.getPassword())).isTrue();
//    }
}
