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

import java.util.Optional;

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
                user.getRole());

        Role role = user.getRole();

        return new LoginResult(token, role);
    }

    @Transactional
    public LoginResult kakaoLogin(String code) {
        KakaoLoginDto kakaoLoginDto = kakaoApiCall.callKakaoLogin(code);
        log.info(kakaoLoginDto.getId_token());
        DecodedJWT jwt = JWT.decode(kakaoLoginDto.getId_token());
        String email = jwt.getClaim("email").asString();
        String sub = jwt.getClaim("sub").asString();

        Optional<User> user = userRepository.findUserByEmail(email);
        //유저가 있으면 UserAuth를 확인하고

        if (user.isPresent()) {
            Optional<UserAuth> userAuth = userAuthRepository.findUserAuthByUserAndProvider(user.get(), Provider.KAKAO);
            // 해당 프로바이더 연동 되어있다면 그냥 토큰 발급
            if (userAuth.isPresent()) {
                String token = createToken(user.get().getUserId(), userAuth.get().getProviderId(), user.get().getEmail(),
                        user.get().getRole());
                return new LoginResult(token, user.get().getRole());
            }
            // 연동 안 되어있다면 연동 시키고 토큰 발급
            else {
                UserAuth userAuth1 = createUserAuthEntity(user.get(), sub, Provider.KAKAO, null);
                saveUsersAuth(userAuth1);
                String token = createToken(user.get().getUserId(), userAuth1.getProviderId(), user.get().getEmail(),
                        user.get().getRole());
                return new LoginResult(token, user.get().getRole());
            }
        }
        //유저가 없으면 User를 생성하고 UserAuth를 생성한다
        else {
            User user1 = createUserEntity(email, nickNameService.getRandomNickName(), Role.ROLE_USER);
            saveUser(user1);
            UserAuth userAuth1 = createUserAuthEntity(user1, sub, Provider.KAKAO, null);
            saveUsersAuth(userAuth1);
            String token = createToken(user1.getUserId(), userAuth1.getProviderId(), user1.getEmail(),
                    user1.getRole());
            return new LoginResult(token, user1.getRole());
        }
    }


    private UserAuth createUserAuthEntity(User user, String providerId, Provider provider, String password) {
        return UserAuth.builder()
                .user(user)
                .providerId(providerId)
                .provider(Provider.LOCAL)
                .password(password)
                .build();
    }

    @Transactional
    public void saveUsersAuth(UserAuth userAuth) {
        userAuthRepository.save(userAuth);
    }

    private String createToken(Long id, String userId, String email,
                               Role role) {

        return jwtUtil.createToken(Subject.atk(id, userId, email, role));
    }

    private User createUserEntity(String email, String nickName, Role role) {

        return User.builder()
                .email(email)
                .nickName(nickName)
                .role(role)
                .score(0)
                .build();
    }


    private void saveUser(User user) {
        userRepository.save(user);
    }


    @Transactional(readOnly = true)
    public UserAuth findUserAuthByProviderIdAndProvider(String providerId, Provider provider) throws BadRequestException {
        return userAuthRepository.findUserAuthByProviderIdAndProvider(providerId, provider)
                .orElseThrow(() -> new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }
}
