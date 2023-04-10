package com.smu.smuenip.domain.image;

import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ReceiptRepositoryTest {

    @Autowired
    ReceiptRepository receiptRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void findReceiptsByCreatedDate() {

        //given
        User user = User.builder()
            .loginId("test1234")
            .role(Role.ROLE_USER)
            .email("test1234@gmail.com")
            .score(0)
            .build();

        Receipt receipt = Receipt.builder()
            .imageUrl("Dadadsfasghdfegewsdwdwq")
            .user(user)
            .build();

        //when

        Receipt savedReceipt = receiptRepository.save(receipt);

        Assertions.assertThat(savedReceipt.getImageUrl()).isEqualTo("Dadadsfasghdfegewsdwdwq");
    }
}
