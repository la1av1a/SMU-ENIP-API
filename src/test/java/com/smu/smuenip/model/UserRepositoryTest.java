package com.smu.smuenip.model;

import com.smu.smuenip.domain.dto.RankDto;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.infrastructure.config.security.BCryptPasswordEncoderConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Import(BCryptPasswordEncoderConfig.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("유저_랭킹_테스트")
    void findUserScore() {
        //given
        int givenScore1 = 40;
        User user1 = User.builder()
                .score(givenScore1)
                .build();

        int givenScore = 20;

        User user2 = User.builder()
                .score(givenScore)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        //when

        List<Object[]> userScoreList = userRepository.findUserScore(10, 0);

        List<RankDto> rankDtoList = userScoreList.stream()
                .map(o -> new RankDto(String.valueOf(o[0]),
                        Integer.parseInt(String.valueOf(o[1])),
                        Long.parseLong(String.valueOf(o[2]))))
                .collect(Collectors.toList());

        RankDto expectDto1 = rankDtoList.get(0);
        RankDto expectDto2 = rankDtoList.get(1);
        //then

        Assertions.assertThat(expectDto1.getRank()).isEqualTo(1);
        
        Assertions.assertThat(expectDto2.getRank()).isEqualTo(2);
    }

}
