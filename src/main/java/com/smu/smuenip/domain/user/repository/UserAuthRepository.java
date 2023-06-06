package com.smu.smuenip.domain.user.repository;

import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.model.UserAuth;
import com.smu.smuenip.enums.Provider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {

    Optional<UserAuth> findUsersAuthsByUser(User user);

    Optional<UserAuth> findUserAuthByProviderIdAndProvider(String providerId, Provider provider);

    Optional<UserAuth> findUserAuthByUserAndProvider(User user, Provider provider);

    boolean existsUserAuthByProviderIdAndProvider(String providerId, Provider provider);

    boolean existsUserAuthsByProviderIdOrUserEmailAndProvider(String providerId, String email,
        Provider provider);
}
