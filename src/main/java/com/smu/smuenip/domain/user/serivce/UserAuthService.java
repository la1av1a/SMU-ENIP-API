package com.smu.smuenip.domain.user.serivce;

import com.smu.smuenip.Infrastructure.config.exception.BadRequestException;
import com.smu.smuenip.Infrastructure.config.jwt.Subject;
import com.smu.smuenip.Infrastructure.config.redis.TokenInfo;
import com.smu.smuenip.Infrastructure.config.redis.TokenInfoRepository;
import com.smu.smuenip.application.auth.dto.UserLoginRequestDto;
import com.smu.smuenip.application.auth.dto.UserRequestDto;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.model.UserAuth;
import com.smu.smuenip.domain.user.repository.UserAuthRepository;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.Provider;
import com.smu.smuenip.enums.Role;
import com.smu.smuenip.enums.meesagesDetail.MessagesFail;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserAuthService {

    private final UserAuthRepository userAuthRepository;
    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;
    private final JwtService jwtService;
    private final TokenInfoRepository tokenInfoRepository;

    @Transactional
    public void createUser(UserRequestDto requestDto) throws BadRequestException {
        if (userRepository.existsUsersByLoginId(requestDto.getLoginId())) {
            throw new BadRequestException(MessagesFail.USER_EXISTS.getMessage());
        }

        User user = createUserEntity(requestDto);
        UserAuth userAuth = createUsersAuthEntity(user, requestDto);
        saveUser(user);
        saveUsersAuth(userAuth);
    }

    @Transactional(readOnly = true)
    public String login(UserLoginRequestDto requestDto) throws BadRequestException {

        User user = findUserByUserId(requestDto.getLoginId());
        UserAuth userAuth = findUserByUser(user);
        if (!passwordEncoderService.matchPassword(requestDto.getPassword(),
            userAuth.getPassword())) {
            throw new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage());
        }

        String token = createToken(user.getId(), user.getLoginId(), user.getEmail(),
            user.getRole());

        TokenInfo tokenInfo = TokenInfo.builder()
            .id(Long.toString(user.getId()))
            .loginId(user.getLoginId())
            .email(user.getEmail())
            .role(user.getRole())
            .accessTokenExpiration(jwtService.getTokenLive())
            .accessToken(token)
            .createdAt(new Date())
            .build();

        tokenInfoRepository.save(tokenInfo);

        return token;
    }


    private UserAuth createUsersAuthEntity(User user, UserRequestDto requestDto) {
        return UserAuth.builder()
            .user(user)
            .provider(Provider.LOCAL)
            .password(passwordEncoderService.encodePassword(requestDto.getPassword()))
            .build();
    }

    private void saveUsersAuth(UserAuth userAuth) {
        userAuthRepository.save(userAuth);
    }

    private String createToken(Long id, String userId, String email,
        Role role) {

        return jwtService.createToken(Subject.atk(id, userId, email, role));
    }

    private User createUserEntity(UserRequestDto requestDto) {

        return User.builder()
            .loginId(requestDto.getLoginId())
            .email(requestDto.getEmail())
            .role(Role.ROLE_USER)
            .build();
    }


    private void saveUser(User user) {
        userRepository.save(user);
    }

    private UserAuth findUserByUser(User user) throws BadRequestException {
        return userAuthRepository.findUsersAuthsByUser(user)
            .orElseThrow(() -> new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    User findUserByUserId(String loginId) throws BadRequestException {
        return userRepository.findUserByLoginId(loginId)
            .orElseThrow(() -> new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }
}
