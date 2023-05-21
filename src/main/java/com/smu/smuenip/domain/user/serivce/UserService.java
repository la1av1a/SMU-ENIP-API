package com.smu.smuenip.domain.user.serivce;

import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesFail;
import com.smu.smuenip.infrastructure.config.exception.UnExpectedErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findUserByUserId(id).orElseThrow(() -> new UnExpectedErrorException(MessagesFail.USER_NOT_FOUND.getMessage()));
    }
}
