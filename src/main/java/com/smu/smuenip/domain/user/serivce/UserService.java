package com.smu.smuenip.domain.user.serivce;

import com.smu.smuenip.Infrastructure.config.exception.BadRequestException;
import com.smu.smuenip.Infrastructure.config.jwt.JwtProvider;
import com.smu.smuenip.Infrastructure.config.jwt.Subject;
import com.smu.smuenip.application.user.dto.UserLoginRequestDto;
import com.smu.smuenip.application.user.dto.UserRequestDto;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.domain.user.repository.UserAuthRepository;
import com.smu.smuenip.enums.meesagesDetail.MessagesFail;
import com.smu.smuenip.enums.Provider;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.model.UserAuth;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public boolean createUser(UserRequestDto requestDto) {
        boolean isExists = userRepository.existsUsersByUserId(requestDto.getUserId());

        if (isExists) {
            return false;
        }

        User user = createUserEntity(requestDto);
        UserAuth userAuth = createUsersAuthEntity(user, requestDto);

        saveUser(user);
        saveUsersAuth(userAuth);
        return true;
    }

    public String login(UserLoginRequestDto requestDto) {
        User user = userRepository.findUserByUserId(requestDto.getUserId())
            .orElseThrow(() -> new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage()));
        Optional<UserAuth> usersAuth = userAuthRepository.findUsersAuthsByUser(user);

        usersAuth.ifPresent(auth -> validatePassword(requestDto.getPassword(), auth));

        return jwtProvider.createToken(
            Subject.atk(user.getId(), user.getUserId(), user.getEmail()));
    }


    private User createUserEntity(UserRequestDto requestDto) {
        return User.builder()
            .userId(requestDto.getUserId())
            .email(requestDto.getEmail())
            .build();
    }


    private UserAuth createUsersAuthEntity(User user, UserRequestDto requestDto) {
        return UserAuth.builder()
            .user(user)
            .provider(Provider.LOCAL)
            .password(passwordEncoder.encode(requestDto.getPassword()))
            .phoneNumber(removeHyphensFromPhoneNumber(requestDto.getPhoneNumber()))
            .build();
    }

    private String removeHyphensFromPhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("-", "");
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }

    private UserAuth findUserByUsers(User user) {
        return userAuthRepository.findUsersAuthsByUser(user)
            .orElseThrow(() -> new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }

    private void saveUser(User user) {
        userRepository.save(user);
    }

    private void saveUsersAuth(UserAuth userAuth) {
        userAuthRepository.save(userAuth);
    }

    private void validatePassword(String rawPassword, UserAuth userAuth) {
        boolean isMatched = passwordEncoder.matches(rawPassword, userAuth.getPassword());

        if (!isMatched) {
            throw new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage());
        }
    }

}
