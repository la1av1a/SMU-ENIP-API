package com.smu.smuenip.domain.approve.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApproveRepository extends JpaRepository<Approve, Long> {

    boolean existsByRecycledImageId(Long recycledImageId);
}
