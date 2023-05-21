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
import java.util.Random;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserAuthService {

    private final UserAuthRepository userAuthRepository;
    private final UserRepository userRepository;
    private final PasswordEncoderConfig passwordEncoderConfig;
    private final JwtUtil jwtUtil;
    private final KakaoApiCall kakaoApiCall;

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
    public LoginResult login(LoginRequestDto requestDto) throws BadRequestException {

        User user = findUserByUserId(requestDto.getLoginId());
        UserAuth userAuth = findUserByUser(user);
        if (!passwordEncoderConfig.matchPassword(requestDto.getPassword(),
                userAuth.getPassword())) {
            throw new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage());
        }

        String token = createToken(user.getUserId(), user.getLoginId(), user.getEmail(),
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
                String token = createToken(user.get().getUserId(), user.get().getLoginId(), user.get().getEmail(),
                        user.get().getRole());
                return new LoginResult(token, user.get().getRole());
            }
            // 연동 안 되어있다면 연동 시키고 토큰 발급
            else {
                UserAuth userAuth1 = UserAuth.builder()
                        .user(user.get())
                        .provider(Provider.KAKAO)
                        .providerId(sub)
                        .build();
                saveUsersAuth(userAuth1);
                String token = createToken(user.get().getUserId(), user.get().getLoginId(), user.get().getEmail(),
                        user.get().getRole());
                return new LoginResult(token, user.get().getRole());
            }
        }
        //유저가 없으면 User를 생성하고 UserAuth를 생성한다
        else {
            Random rnd = new Random();
            User user1 = User.builder()
                    .email(email)
                    .loginId(String.valueOf((char) ((int) (rnd.nextInt(10)) + 97)))
                    .role(Role.ROLE_USER)
                    .build();
            saveUser(user1);
            UserAuth userAuth1 = UserAuth.builder()
                    .user(user1)
                    .provider(Provider.KAKAO)
                    .providerId(sub)
                    .build();
            saveUsersAuth(userAuth1);
            String token = createToken(user1.getUserId(), user1.getLoginId(), user1.getEmail(),
                    user1.getRole());
            return new LoginResult(token, user1.getRole());
        }
    }


    private UserAuth createUsersAuthEntity(User user, UserRequestDto requestDto) {
        return UserAuth.builder()
                .user(user)
                .provider(Provider.LOCAL)
                .password(passwordEncoderConfig.encodePassword(requestDto.getPassword()))
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

    @Transactional(readOnly = true)
    public UserAuth findUserByUser(User user) throws BadRequestException {
        return userAuthRepository.findUsersAuthsByUser(user)
                .orElseThrow(() -> new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    public User findUserByUserId(String loginId) throws BadRequestException {
        return userRepository.findUserByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }
}
