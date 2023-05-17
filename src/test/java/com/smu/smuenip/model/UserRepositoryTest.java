package com.smu.smuenip.model;

import com.smu.smuenip.Infrastructure.config.security.BCryptPasswordEncoderConfig;
import com.smu.smuenip.domain.dto.RankDto;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

@Import(BCryptPasswordEncoderConfig.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("유저가 잘 저장되는지 확인")
    void userSaveTest() {
        //given

        String loginId = "test1234";

        User user = User.builder()
            .loginId(loginId)
            .email("test1234@gmail.com")
            .score(2)
            .build();

        //when
        userRepository.save(user);

        User expect = userRepository.findUserByLoginId(loginId)
            .orElseThrow(NoSuchElementException::new);

        //then
        Assertions.assertThat(expect.getLoginId()).isEqualTo(loginId);
    }

    @Test
    @DisplayName("유저_랭킹_테스트")
    void findUserScore() {
        //given
        String givenLoginId1 = "dasdsa";
        int givenScore1 = 40;
        User user1 = User.builder()
            .loginId(givenLoginId1)
            .score(givenScore1)
            .build();

        String givenLoginId2 = "test1234";
        int givenScore = 20;

        User user2 = User.builder()
            .loginId(givenLoginId2)
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

        Assertions.assertThat(expectDto1.getLogin_id()).isEqualTo(givenLoginId1);
        Assertions.assertThat(expectDto1.getRank()).isEqualTo(1);

        Assertions.assertThat(expectDto2.getLogin_id()).isEqualTo(givenLoginId2);
        Assertions.assertThat(expectDto2.getRank()).isEqualTo(2);
    }

}
