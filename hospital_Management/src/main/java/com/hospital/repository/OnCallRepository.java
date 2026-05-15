package com.hospital.repository;

import com.hospital.entity.OnCall;
import com.hospital.entity.OnCallId;
import com.hospital.projection.OnCallProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for OnCall entity.
 * POST, PUT, DELETE, PATCH handled by OnCallController.
 */
@RepositoryRestResource(path = "oncall", excerptProjection = OnCallProjection.class)
public interface OnCallRepository extends JpaRepository<OnCall, OnCallId> {

    // ── Projection-based reads ─────────────────────────────────────────────
    Page<OnCallProjection> findAllProjectedBy(Pageable pageable);
    Optional<OnCallProjection> findProjectedById(OnCallId id);

    // ── By Nurse ──────────────────────────────────────────────────────────────
    List<OnCall> findById_NurseId(Integer nurseId);
    long countById_NurseId(Integer nurseId);

    // ── By Block ──────────────────────────────────────────────────────────────
    List<OnCall> findById_BlockFloorAndId_BlockCode(Integer blockFloor, Integer blockCode);
    List<OnCall> findById_BlockFloor(Integer blockFloor);
    List<OnCall> findById_BlockCode(Integer blockCode);

    // ── By Time Window ────────────────────────────────────────────────────────
    List<OnCall> findByOnCallStartBetween(LocalDateTime from, LocalDateTime to);

    // ── Combined ──────────────────────────────────────────────────────────────
    List<OnCall> findById_NurseIdAndId_BlockFloorAndId_BlockCode(Integer nurseId, Integer blockFloor, Integer blockCode);

    List<OnCall> findByOnCallStartLessThanEqualAndOnCallEndGreaterThanEqual(
            LocalDateTime now, LocalDateTime alsoNow);

    List<OnCall> findById_NurseIdAndOnCallStartBeforeAndOnCallEndAfter(
            Integer nurseId, LocalDateTime windowEnd, LocalDateTime windowStart);

    List<OnCall> findAllByOrderByOnCallStartAsc();
}
