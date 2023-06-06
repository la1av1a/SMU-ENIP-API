package com.smu.smuenip.domain.purchasedItem.model;

import java.util.List;
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
        + "AND r.user.userId = :userId",
        countQuery = "SELECT COUNT(p) "
            + "FROM PurchasedItem p "
            + "JOIN p.receipt r "
            + "WHERE YEAR(r.purchasedDate) = :year "
            + "AND MONTH(r.purchasedDate) = :month "
            + "AND DAY(r.purchasedDate) = :day "
            + "AND r.user.userId = :userId"
    )
    Page<PurchasedItem> findPurchasedItemsByCreatedDate(@Param("year") int year,
        @Param("month") int month, @Param("day") int day, @Param("userId") Long userId,
        Pageable pageable);

    Page<PurchasedItem> findPurchasedItemByUserUserId(Long userId, Pageable pageable);

    @Query("SELECT p FROM PurchasedItem p "
        + "LEFT JOIN FETCH p.recycledImage r "
        + "WHERE p.user.userId = :userId AND r IS NULL")
    List<PurchasedItem> findNotRecycledItemsByUserUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM PurchasedItem p "
        + "LEFT JOIN FETCH p.recycledImage r "
        + "WHERE p.user.userId =:userId AND r IS NOT NULL")
    List<PurchasedItem> findRecycledItemsByUserUserId(@Param("userId") Long userId);
}
