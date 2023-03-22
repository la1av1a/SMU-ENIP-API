package com.smu.smuenip.user.domain.repository;


import com.smu.smuenip.user.domain.model.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findUserByUserId(String userId);

    boolean existsUsersByUserId(String userId);
}
