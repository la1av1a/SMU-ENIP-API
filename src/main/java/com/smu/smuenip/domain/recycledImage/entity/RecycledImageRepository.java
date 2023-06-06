package com.smu.smuenip.domain.recycledImage.entity;

import com.smu.smuenip.application.recycle.dto.RecycledImageResponseDto;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecycledImageRepository extends JpaRepository<RecycledImage, Long> {

    @Query(value =
        "SELECT new com.smu.smuenip.application.recycle.dto.RecycledImageResponseDto(p.purchasedDate, u.userId, r.id, r.recycledImageUrl, r.isChecked, r.isApproved) "
            +
            "FROM RecycledImage r " +
            "JOIN r.purchasedItem p " +
            "JOIN p.user u " +
            "WHERE u.userId = :userId")
    List<RecycledImageResponseDto> findRecycledResponseDtoByUserId(Long userId);

    @Query(value =
        "SELECT new com.smu.smuenip.application.recycle.dto.RecycledImageResponseDto(p.purchasedDate, u.userId, r.id, r.recycledImageUrl, r.isChecked, r.isApproved) "
            +
            "FROM RecycledImage r " +
            "JOIN r.purchasedItem p " +
            "JOIN p.user u " +
            "WHERE u.userId = :userId AND r.isChecked = true AND r.isApproved = :isApproved")
    List<RecycledImageResponseDto> findRecycledResponseDtoByCheckedAndApprovedAndUserId(Long userId,
        boolean isApproved);

    @Query(value =
        "SELECT new com.smu.smuenip.application.recycle.dto.RecycledImageResponseDto(p.purchasedDate, u.userId, r.id, r.recycledImageUrl, r.isChecked, r.isApproved) "
            +
            "FROM RecycledImage r " +
            "JOIN r.purchasedItem p " +
            "JOIN p.user u " +
            "WHERE u.userId = :userId AND r.uploadDate = :date")
    List<RecycledImageResponseDto> findRecycledResponseDtoByUserIdAndDate(Long userId,
        LocalDate date);

    @Query(value =
        "SELECT new com.smu.smuenip.application.recycle.dto.RecycledImageResponseDto(p.purchasedDate, u.userId, r.id, r.recycledImageUrl, r.isChecked, r.isApproved) "
            +
            "FROM RecycledImage r " +
            "JOIN r.purchasedItem p " +
            "JOIN p.user u " +
            "WHERE u.userId = :userId AND r.isApproved = :isRecycled AND r.uploadDate = :date")
    List<RecycledImageResponseDto> findRecycledResponseDtoByUserIdAndDateAndIsRecycled(Long userId,
        LocalDate date, boolean isRecycled);

    @Query(value =
        "SELECT new com.smu.smuenip.application.recycle.dto.RecycledImageResponseDto"
            + "(p.purchasedDate,u.userId, r.id, r.recycledImageUrl, r.isChecked, r.isApproved)" +
            "FROM RecycledImage r " +
            "LEFT JOIN r.purchasedItem p " +
            "LEFT JOIN p.user u")
    List<RecycledImageResponseDto> findAllRecycledImages();

    @Query(value =
        "SELECT new com.smu.smuenip.application.recycle.dto.RecycledImageResponseDto(p.purchasedDate, u.userId, r.id, r.recycledImageUrl, r.isChecked, r.isApproved) "
            +
            "FROM RecycledImage r " +
            "JOIN r.purchasedItem p " +
            "JOIN p.user u " +
            "WHERE r.isApproved =:isRecycled")
    List<RecycledImageResponseDto> findRecycledImagesByCheckedAndApproved(boolean isRecycled);

    @Query(value =
        "SELECT new com.smu.smuenip.application.recycle.dto.RecycledImageResponseDto(p.purchasedDate, u.userId, r.id, r.recycledImageUrl, r.isChecked, r.isApproved) "
            +
            "FROM RecycledImage r " +
            "JOIN r.purchasedItem p " +
            "JOIN p.user u " +
            "WHERE r.uploadDate = :date")
    List<RecycledImageResponseDto> findRecycledImagesByDate(LocalDate date);

    @Query(value =
        "SELECT new com.smu.smuenip.application.recycle.dto.RecycledImageResponseDto(p.purchasedDate, u.userId, r.id, r.recycledImageUrl, r.isChecked, r.isApproved) "
            +
            "FROM RecycledImage r " +
            "JOIN r.purchasedItem p " +
            "JOIN p.user u " +
            "WHERE r.uploadDate = :date AND r.isApproved =:isRecycled")
    List<RecycledImageResponseDto> findRecycledImagesByDateAndIsRecycled(LocalDate date,
        boolean isRecycled);

}
