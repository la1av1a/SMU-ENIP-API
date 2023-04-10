package com.smu.smuenip.domain.PurchasedItem.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PurchasedItemRepository extends JpaRepository<PurchasedItem, Long> {

    @Query(value = "SELECT p "
        + "FROM PurchasedItem p "
        + "JOIN FETCH p.receipt r "
        + "WHERE YEAR(r.purchasedDate) = :year "
        + "AND MONTH(r.purchasedDate) = :month "
        + "AND DAY(r.purchasedDate) = :day "
        + "AND r.user.id = :userId",
        countQuery = "SELECT COUNT(p) "
            + "FROM PurchasedItem p "
            + "JOIN p.receipt r "
            + "WHERE YEAR(r.purchasedDate) = :year "
            + "AND MONTH(r.purchasedDate) = :month "
            + "AND DAY(r.purchasedDate) = :day "
            + "AND r.user.id = :userId"
    )
    Page<PurchasedItem> findPurchasedItemsByCreatedDate(@Param("year") int year,
        @Param("month") int month, @Param("day") int day, @Param("userId") Long userId,
        Pageable pageable);


}
