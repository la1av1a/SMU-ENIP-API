package com.smu.smuenip.domain.receipt;

import com.smu.smuenip.domain.receipt.model.Receipt;
import com.smu.smuenip.domain.receipt.repository.ReceiptRepository;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
class ReceiptRepositoryTest {

    @Autowired
    ReceiptRepository receiptRepository;

    @Autowired
    UserRepository userRepository;

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

        Receipt receipt = Receipt.builder()
            .user(savedUser) // 저장된 User 객체 사용
            .receipt_url("test_url")
            .build();

        //when
        Receipt savedReceipt = receiptRepository.save(receipt);

        //then
        Assertions.assertThat(savedReceipt.getReceipt_url()).isEqualTo("test_url");
        Assertions.assertThat(savedReceipt.getUploadedDate()).isNotNull();
    }
}
