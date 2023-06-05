package com.smu.smuenip.domain.user.serivce;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.smu.smuenip.application.login.dto.LoginRequestDto;
import com.smu.smuenip.application.login.dto.LoginResult;
import com.smu.smuenip.application.login.dto.UserRequestDto;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.model.UserAuth;
import com.smu.smuenip.domain.user.repository.UserAuthRepository;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.Provider;
import com.smu.smuenip.enums.Role;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesFail;
import com.smu.smuenip.infrastructure.config.PasswordEncoderConfig;
import com.smu.smuenip.infrastructure.config.exception.BadRequestException;
import com.smu.smuenip.infrastructure.util.jwt.JwtUtil;
import com.smu.smuenip.infrastructure.util.jwt.Subject;
import com.smu.smuenip.infrastructure.util.kakao.KakaoApiCall;
import com.smu.smuenip.infrastructure.util.kakao.KakaoLoginDto;
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
    private final PasswordEncoderConfig passwordEncoderConfig;
    private final JwtUtil jwtUtil;
    private final KakaoApiCall kakaoApiCall;
    private final NickNameService nickNameService;

    @Transactional
    public void createUser(UserRequestDto requestDto) throws BadRequestException {
        if (userAuthRepository.existsUserAuthByProviderIdAndProvider(requestDto.getLoginId(), Provider.LOCAL)) {
            throw new BadRequestException(MessagesFail.USER_EXISTS.getMessage());
        }

        String nickName = nickNameService.getRandomNickName();
        User user = createUserEntity(requestDto.getEmail(), nickName, Role.ROLE_USER);

        String encodedPassword = passwordEncoderConfig.encodePassword(requestDto.getPassword());
        UserAuth userAuth = createUserAuthEntity(user, requestDto.getLoginId(), Provider.LOCAL, encodedPassword);

        saveUser(user);
        saveUsersAuth(userAuth);
    }

    @Transactional(readOnly = true)
    public LoginResult login(LoginRequestDto requestDto) throws BadRequestException {

        UserAuth userAuth = findUserAuthByProviderIdAndProvider(requestDto.getLoginId(), Provider.LOCAL);
        User user = userAuth.getUser();
        if (!passwordEncoderConfig.matchPassword(requestDto.getPassword(),
                userAuth.getPassword())) {
            throw new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage());
        }

        String token = createToken(user.getUserId(), userAuth.getProviderId(), user.getEmail(),
                user.getRole(), Provider.LOCAL);

        Role role = user.getRole();

        return new LoginResult(token, role, user.getNickName(), user.getProfileImageUrl(), user.getScore());
    }

    public void deleteUser(Long userId) {

        if (!userRepository.existsById(userId))
            throw new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage());

        userRepository.deleteById(userId);
    }

    @Transactional
    public LoginResult kakaoLogin(String code) {
        KakaoLoginDto kakaoLoginDto = kakaoApiCall.callKakaoLogin(code);
        log.info(kakaoLoginDto.getId_token());
        DecodedJWT jwt = JWT.decode(kakaoLoginDto.getId_token());
        String email = jwt.getClaim("email").asString();
        String sub = jwt.getClaim("sub").asString();

        User user = userRepository.findUserByEmail(email)
                .orElseGet(() -> saveUser(createUserEntity(email, nickNameService.getRandomNickName(), Role.ROLE_USER)));
        UserAuth userAuth = userAuthRepository.findUserAuthByUserAndProvider(user, Provider.KAKAO)
                .orElseGet(() -> saveUsersAuth(createUserAuthEntity(user, sub, Provider.KAKAO, null)));

        String token = createToken(user.getUserId(), userAuth.getProviderId(), user.getEmail(),
                user.getRole(), Provider.KAKAO);

        return new LoginResult(token, user.getRole(), user.getNickName(), user.getProfileImageUrl(), user.getScore());
    }


    private UserAuth createUserAuthEntity(User user, String providerId, Provider provider, String password) {
        return UserAuth.builder()
                .user(user)
                .providerId(providerId)
                .provider(provider)
                .password(password)
                .build();
    }

    @Transactional
    public UserAuth saveUsersAuth(UserAuth userAuth) {
        return userAuthRepository.save(userAuth);
    }

    private String createToken(Long id, String userId, String email,
                               Role role, Provider provider) {

        return jwtUtil.createToken(Subject.atk(id, userId, email, role, provider));
    }

    private User createUserEntity(String email, String nickName, Role role) {

        return User.builder()
                .email(email)
                .nickName(nickName)
                .role(role)
                .score(0)
                .weight(0)
                .build();
    }


    private User saveUser(User user) {
        return userRepository.save(user);
    }


    @Transactional(readOnly = true)
    public UserAuth findUserAuthByProviderIdAndProvider(String providerId, Provider provider) throws BadRequestException {
        return userAuthRepository.findUserAuthByProviderIdAndProvider(providerId, provider)
                .orElseThrow(() -> new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }
}
