package com.smu.smuenip.domain.user.repository;

import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.model.UserAuth;
import com.smu.smuenip.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {

    Optional<UserAuth> findUsersAuthsByUser(User user);

    Optional<UserAuth> findUserAuthByUserAndProvider(User user, Provider provider);
}
