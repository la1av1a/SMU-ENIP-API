package com.smu.smuenip.domain.recycledImage;

import com.smu.smuenip.domain.image.Receipt;
import com.smu.smuenip.domain.image.ReceiptRepository;
import com.smu.smuenip.domain.recycledImage.entity.RecycledImage;
import com.smu.smuenip.domain.recycledImage.entity.RecycledImageRepository;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class RecycledImageRepositoryTest {

    @Autowired
    RecycledImageRepository recycledImageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReceiptRepository receiptRepository;

    @DisplayName("분리수거_이미지가_잘_저장되는지_확인")
    void save_test() {

        User user = User.builder()
                .loginId("testtest")
                .role(Role.ROLE_USER)
                .score(0)
                .email("testest@gmail.com")
                .build();

        userRepository.save(user);

        Receipt receipt = Receipt.builder()
                .imageUrl("dsadasdsa")
                .user(user)
                .build();

        receiptRepository.save(receipt);

        String recycledImageUrl = "dsadisojadiasjd";
        boolean isChecked = true;

        RecycledImage recycledImage = RecycledImage.builder()
                .recycledImageUrl("faifaifjas")
                .isChecked(true)
                .receipt(receipt)
                .user(user)
                .build();

        RecycledImage savedRecycledImage = recycledImageRepository.save(recycledImage);

        Assertions.assertThat(savedRecycledImage.getRecycledImageUrl()).isEqualTo(recycledImageUrl);
        Assertions.assertThat(savedRecycledImage.getUser()).isEqualTo(user);
        Assertions.assertThat(savedRecycledImage.getReceipt()).isEqualTo(receipt);
        Assertions.assertThat(savedRecycledImage.isApproved()).isEqualTo(null);
        Assertions.assertThat(savedRecycledImage.getId()).isEqualTo(1L);
    }
}