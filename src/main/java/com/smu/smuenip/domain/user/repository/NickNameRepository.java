package com.smu.smuenip.domain.user.repository;

import com.smu.smuenip.domain.user.model.NickNameExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NickNameRepository extends JpaRepository<NickNameExample, Long> {

    @Query(value = "SELECT n.nick " +
            "FROM nick_name_example n " +
            "WHERE n.depth = 1 " +
            "ORDER BY RAND() " +
            "LIMIT 1", nativeQuery = true)
    String findRandomNickDepth1();

    @Query(value = "SELECT n.nick " +
            "FROM nick_name_example n " +
            "WHERE n.depth = 2 " +
            "ORDER BY RAND() " +
            "LIMIT 1", nativeQuery = true)
    String findRandomNickDepth2();


}
