package com.smu.smuenip.domain.user.repository;


import com.smu.smuenip.domain.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUserId(String userId);

    boolean existsUsersByUserId(String userId);
}
