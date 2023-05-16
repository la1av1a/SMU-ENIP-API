package com.smu.smuenip.domain.user.repository;


import com.smu.smuenip.domain.user.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByLoginId(String loginId);

    Optional<User> findUserByUserId(Long id);

    boolean existsUsersByLoginId(String loginId);

    @Query(value = "SELECT u.login_id, u.score, RANK() OVER (ORDER BY score DESC) AS ranking "
        + "FROM user u "
        + "ORDER BY ranking "
        + "LIMIT :size OFFSET :offset", nativeQuery = true)
    List<Object[]> findUserScore(@Param("size") int size, @Param("offset") int offset);
}
