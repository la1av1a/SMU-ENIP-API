package com.smu.smuenip.domain.receipt;

import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ImageRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageRepository imageRepository;

    @Test
    void 저장_테스트() {
        //given
        User user = User.builder()
            .loginId("test")
            .email("test@example.com")
            .role(Role.ROLE_USER)
            .score(0)
            .build();

        User savedUser = userRepository.save(user); // User 저장

        Image image = Image.builder()
            .user(savedUser) // 저장된 User 객체 사용
            .imageUrl("test_url")
            .isRecycled(false)
            .build();

        //when

        Image savedImage = imageRepository.save(image);

        //then

        Assertions.assertThat(savedImage.getImageUrl()).isEqualTo("test_url");
        Assertions.assertThat(savedImage.getCreatedDate()).isNotNull();
        Assertions.assertThat(savedImage.getUser().getLoginId()).isEqualTo("test");


    }
}
