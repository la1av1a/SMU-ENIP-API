package com.smu.smuenip.domain.image;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    @Query("SELECT r FROM Receipt r WHERE YEAR(r.createdDate) = :year AND MONTH(r.createdDate) = :month AND DAY(r.createdDate) = :day")
    List<Receipt> findReceiptsByCreatedDate(@Param("year") int year,
        @Param("month") int month, @Param("day") int day);
}
