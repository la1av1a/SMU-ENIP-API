package com.smu.smuenip.domain.image;

import com.smu.smuenip.domain.receipt.model.Receipt;
import com.smu.smuenip.domain.receipt.model.ReceiptRepository;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.Role;
import java.time.LocalDate;
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
            .loginId("test12345")
            .role(Role.ROLE_USER)
            .email("test12345@gmail.com")
            .score(0)
            .build();

        Receipt receipt = Receipt.builder()
            .imageUrl("Dadadsfasghdfegewsdwdwq")
            .user(user)
            .purchasedDate(LocalDate.now())
            .build();

        //when

        Receipt savedReceipt = receiptRepository.save(receipt);

        Assertions.assertThat(savedReceipt.getImageUrl()).isEqualTo("Dadadsfasghdfegewsdwdwq");
    }
}
