package com.smu.smuenip.Infrastructure.config.redis;

import com.smu.smuenip.Infrastructure.config.jwt.Subject;
import com.smu.smuenip.domain.auth.service.JwtService;
import java.time.Instant;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenInfoRepositoryTest {

    @Autowired
    TokenInfoRepository tokenInfoRepository;
    @Autowired
    JwtService jwtService;

    @Test
    void save() {
        //given
        String userId = "test1234";
        String email = "test1234@gmail.com";
        Role role = Role.ROLE_USER;
        String accessToken = jwtService.createToken(Subject.atk(5L, userId, email, role));
        String refreshToken = jwtService.createToken(Subject.rtk(5L, userId, email, role));
        TokenInfo tokenInfo = TokenInfo.builder()
            .id(null)
            .userId(userId)
            .email(email)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .accessTokenExpiration(Instant.now())
            .refreshTokenExpiration(Instant.now())
            .build();

        //when
        TokenInfo savedTokenInfo = tokenInfoRepository.save(tokenInfo);

        //then
        Optional<TokenInfo> findTokenInfo = tokenInfoRepository.findById(savedTokenInfo.getId());

        Assertions.assertThat(findTokenInfo)
            .isPresent()
            .satisfies(tokenInfoOpt -> {
                TokenInfo getTokenInfo = tokenInfoOpt.get();
                Assertions.assertThat(getTokenInfo.getEmail()).isEqualTo(email);
                Assertions.assertThat(getTokenInfo.getAccessToken()).isEqualTo(accessToken);
            });
    }
}
