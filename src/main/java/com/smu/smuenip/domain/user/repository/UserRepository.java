package com.smu.smuenip.domain.user.repository;


import com.smu.smuenip.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUserId(Long id);

    Optional<User> findUserByEmail(String email);

}
