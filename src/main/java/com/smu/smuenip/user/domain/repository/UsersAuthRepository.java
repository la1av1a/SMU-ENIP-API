package com.smu.smuenip.user.domain.repository;

import com.smu.smuenip.user.domain.model.Users;
import com.smu.smuenip.user.domain.model.UsersAuth;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersAuthRepository extends JpaRepository<UsersAuth, Long> {

    Optional<UsersAuth> findUsersAuthsByUser(Users user);
}
