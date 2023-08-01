package com.smu.smuenip.domain.purchasedItem.model;

import java.util.List;
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
    List<PurchasedItem> findPurchasedItemsByCreatedDate(@Param("year") int year,
        @Param("month") int month, @Param("day") int day, @Param("userId") Long userId);

    @Query("SELECT p FROM PurchasedItem p "
        + "LEFT JOIN FETCH p.recycledImage r "
        + "LEFT JOIN FETCH p.receipt re "
        + "LEFT JOIN FETCH p.category c "
        + "WHERE p.user.userId = :userId")
    List<PurchasedItem> findPurchasedItemByUserUserId(Long userId);

    @Query("SELECT p FROM PurchasedItem p "
        + "LEFT JOIN FETCH p.recycledImage r "
        + "WHERE p.user.userId =:userId AND r IS NULL")
    List<PurchasedItem> findNotRecycledItemsByUserUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM PurchasedItem p "
        + "LEFT JOIN FETCH p.recycledImage r "
        + "WHERE p.user.userId =:userId AND r IS NOT NULL")
    List<PurchasedItem> findRecycledItemsByUserUserId(@Param("userId") Long userId);
}
