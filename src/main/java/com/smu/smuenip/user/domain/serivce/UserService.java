package com.smu.smuenip.user.domain.serivce;

import com.smu.smuenip.user.application.dto.UserRequestDto;
import com.smu.smuenip.user.domain.enums.Provider;
import com.smu.smuenip.user.domain.model.Users;
import com.smu.smuenip.user.domain.model.UsersAuth;
import com.smu.smuenip.user.domain.repository.UserRepository;
import com.smu.smuenip.user.domain.repository.UsersAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UsersAuthRepository usersAuthRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean createUser(UserRequestDto requestDto) {
        boolean isExists = userRepository.existsUsersByEmail(requestDto.getEmail());

        if (isExists) {
            return false;
        }

        Users users = createUserEntity(requestDto);
        UsersAuth usersAuth = createUsersAuthEntity(users, requestDto);

        saveUser(users);
        saveUsersAuth(usersAuth);
        return true;
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

    private void saveUser(Users users) {
        userRepository.save(users);
    }

    private void saveUsersAuth(UsersAuth usersAuth) {
        usersAuthRepository.save(usersAuth);
    }

}
