package com.smu.smuenip.domain.user.serivce;

import com.smu.smuenip.application.login.dto.UserInfoResponseDto;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesFail;
import com.smu.smuenip.infrastructure.config.exception.UnExpectedErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserInfoResponseDto getUserInfo(Long id) {
        User user = findUserById(id);
        return new UserInfoResponseDto(user);
    }

    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findUserByUserId(id).orElseThrow(() -> new UnExpectedErrorException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    public List<Object[]> findUserScore(int size, int offset) {
        return userRepository.findUserScore(size, offset);
    }
}
