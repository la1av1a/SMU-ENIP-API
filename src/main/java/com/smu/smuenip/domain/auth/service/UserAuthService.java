package com.smu.smuenip.domain.auth.service;

import com.smu.smuenip.Infrastructure.config.exception.BadRequestException;
import com.smu.smuenip.Infrastructure.config.jwt.Subject;
import com.smu.smuenip.application.auth.dto.TokenResponse;
import com.smu.smuenip.application.auth.dto.UserLoginRequestDto;
import com.smu.smuenip.application.auth.dto.UserRequestDto;
import com.smu.smuenip.domain.user.model.Role;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.model.UserAuth;
import com.smu.smuenip.domain.user.repository.RoleRepository;
import com.smu.smuenip.domain.user.repository.UserAuthRepository;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.domain.user.serivce.PasswordEncoderService;
import com.smu.smuenip.enums.Provider;
import com.smu.smuenip.enums.meesagesDetail.MessagesFail;
import java.util.Collection;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserAuthService {

    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoderService passwordEncoderService;
    private final JwtService jwtService;

    private final RoleRepository roleRepository;

    @Transactional
    public void createUser(UserRequestDto requestDto) {
        if (userRepository.existsUsersByUserId(requestDto.getUserId())) {
            throw new BadRequestException(MessagesFail.USER_EXISTS.getMessage());
        }

        User user = createUserEntity(requestDto);
        UserAuth userAuth = createUsersAuthEntity(user, requestDto);
        saveUser(user);
        saveUsersAuth(userAuth);
    }

    @Transactional(readOnly = true)
    public TokenResponse login(UserLoginRequestDto requestDto) {
        User user = findUserByUserId(requestDto.getUserId());
        UserAuth userAuth = findUserByUser(user);
        if (!passwordEncoderService.matchPassword(requestDto.getPassword(),
            userAuth.getPassword())) {
            throw new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage());
        }

        return createTokens(user.getId(), user.getLoginId(), user.getEmail(),
            user.getAuthorities());
    }

    private User findUserByUserId(String userId) {
        return userRepository.findUserByUserId(userId)
            .orElseThrow(() -> new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }

    private User createUserEntity(UserRequestDto requestDto) {
        return User.builder()
            .loginId(requestDto.getUserId())
            .email(requestDto.getEmail())
            .build();
    }

    private UserAuth createUsersAuthEntity(User user, UserRequestDto requestDto) {
        return UserAuth.builder()
            .user(user)
            .provider(Provider.LOCAL)
            .password(passwordEncoderService.encodePassword(requestDto.getPassword()))
            .phoneNumber(removeHyphensFromPhoneNumber(requestDto.getPhoneNumber()))
            .build();
    }

    private String removeHyphensFromPhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("-", "");
    }


    private UserAuth findUserByUser(User user) {
        return userAuthRepository.findUsersAuthsByUser(user)
            .orElseThrow(() -> new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }

    private void saveUser(User user) {
        userRepository.save(user);
    }

    private void saveUsersAuth(UserAuth userAuth) {
        userAuthRepository.save(userAuth);
    }

    private TokenResponse createTokens(Long id, String userId, String email,
        Collection<Role> authorities) {

        String atk = jwtService.createToken(Subject.atk(id, userId, email, authorities));
        String rfk = jwtService.createToken(Subject.rtk(id, userId, email, authorities));

        return TokenResponse.builder()
            .accessToken(atk)
            .refreshToken(rfk)
            .build();
    }

    private Role findRoleByName(String name) {
        return roleRepository.findRoleByName(name).orElseThrow(NoSuchElementException::new);
    }

}
