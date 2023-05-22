package com.smu.smuenip.domain.user.repository;


import com.smu.smuenip.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUserId(Long id);

    Optional<User> findUserByEmail(String email);


    /**
     * @param size
     * @param offset
     * @return nickName, score, rank, userId
     */
    @Query(value = "SELECT u.nick_name, u.score, RANK() OVER (ORDER BY score DESC) AS ranking "
            + "FROM user u "
            + "ORDER BY ranking "
            + "LIMIT :size OFFSET :offset", nativeQuery = true)
    List<Object[]> findUserScore(@Param("size") int size, @Param("offset") int offset);
}
