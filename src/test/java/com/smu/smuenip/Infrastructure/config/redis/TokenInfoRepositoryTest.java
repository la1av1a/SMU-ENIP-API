package com.smu.smuenip.Infrastructure.config.redis;

import com.smu.smuenip.Infrastructure.config.jwt.Subject;
import com.smu.smuenip.domain.auth.service.JwtService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

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
        Collection<GrantedAuthority> role = new ArrayList<>();
        role.add(new SimpleGrantedAuthority("ROLE_USER"));
        String accessToken = jwtService.createToken(Subject.atk(5L, userId, email, role));
        String refreshToken = jwtService.createToken(Subject.rtk(5L, userId, email, role));
        TokenInfo tokenInfo = TokenInfo.builder()
            .id(null)
            .loginId(userId)
            .email(email)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .accessTokenExpiration(1342141412L)
            .refreshTokenExpiration(66934140124L)
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
