package com.smu.smuenip.user.domain.serivce;

import com.smu.smuenip.infra.config.exception.BadRequestException;
import com.smu.smuenip.infra.config.jwt.JwtProvider;
import com.smu.smuenip.infra.config.jwt.Subject;
import com.smu.smuenip.user.application.dto.UserLoginRequestDto;
import com.smu.smuenip.user.application.dto.UserRequestDto;
import com.smu.smuenip.user.application.enums.MessagesFail;
import com.smu.smuenip.user.domain.enums.Provider;
import com.smu.smuenip.user.domain.model.Users;
import com.smu.smuenip.user.domain.model.UsersAuth;
import com.smu.smuenip.user.domain.repository.UserRepository;
import com.smu.smuenip.user.domain.repository.UsersAuthRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UsersAuthRepository usersAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public boolean createUser(UserRequestDto requestDto) {
        boolean isExists = userRepository.existsUsersByUserId(requestDto.getUserId());

        if (isExists) {
            return false;
        }

        Users users = createUserEntity(requestDto);
        UsersAuth usersAuth = createUsersAuthEntity(users, requestDto);

        saveUser(users);
        saveUsersAuth(usersAuth);
        return true;
    }

    public String login(UserLoginRequestDto requestDto) {
        Users users = userRepository.findUserByUserId(requestDto.getUserId())
            .orElseThrow(() -> new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage()));
        Optional<UsersAuth> usersAuth = usersAuthRepository.findUsersAuthsByUser(users);

        usersAuth.ifPresent(auth -> validatePassword(requestDto.getPassword(), auth));

        return jwtProvider.createToken(
            Subject.atk(users.getId(), users.getUserId(), users.getEmail()));
    }


    private Users createUserEntity(UserRequestDto requestDto) {
        return Users.builder()
            .userId(requestDto.getUserId())
            .email(requestDto.getEmail())
            .build();
    }


    private UsersAuth createUsersAuthEntity(Users users, UserRequestDto requestDto) {
        return UsersAuth.builder()
            .user(users)
            .provider(Provider.LOCAL)
            .password(passwordEncoder.encode(requestDto.getPassword()))
            .phoneNumber(removeHyphensFromPhoneNumber(requestDto.getPhoneNumber()))
            .build();
    }

    private String removeHyphensFromPhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("-", "");
    }

    public Users findUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }

    private UsersAuth findUserByUsers(Users users) {
        return usersAuthRepository.findUsersAuthsByUser(users)
            .orElseThrow(() -> new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }

    private void saveUser(Users users) {
        userRepository.save(users);
    }

    private void saveUsersAuth(UsersAuth usersAuth) {
        usersAuthRepository.save(usersAuth);
    }

    private void validatePassword(String rawPassword, UsersAuth usersAuth) {
        boolean isMatched = passwordEncoder.matches(rawPassword, usersAuth.getPassword());

        if (!isMatched) {
            throw new BadRequestException(MessagesFail.USER_NOT_FOUND.getMessage());
        }
    }

}
