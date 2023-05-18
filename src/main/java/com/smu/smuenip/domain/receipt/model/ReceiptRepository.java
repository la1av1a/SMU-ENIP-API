package com.smu.smuenip.domain.receipt.model;

import com.smu.smuenip.domain.user.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    @Query("SELECT r "
        + "FROM Receipt r "
        + "WHERE (YEAR(r.purchasedDate) = :year "
        + "AND MONTH(r.purchasedDate) = :month "
        + "AND DAY(r.purchasedDate) = :day)"
        + "AND r.user = :user"
    )
    Page<Receipt> findReceiptsByCreatedDate(@Param("year") int year,
        @Param("month") int month, @Param("day") int day, @Param("user") User user,
        Pageable pageable);

    Optional<Receipt> findReceiptByIdAndUserUserId(Long receiptId,
        Long userId);
}
