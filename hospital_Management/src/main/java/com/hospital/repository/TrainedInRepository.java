package com.hospital.repository;

import com.hospital.entity.TrainedIn;
import com.hospital.entity.TrainedInId;
import com.hospital.projection.TrainedInProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for TrainedIn entity.
 * POST, PUT, DELETE, PATCH handled by TrainedInController.
 */
@RepositoryRestResource(path = "trainedin")
public interface TrainedInRepository extends JpaRepository<TrainedIn, TrainedInId> {

    // ── Projection-based reads ─────────────────────────────────────────────
    Page<TrainedInProjection> findAllProjectedBy(Pageable pageable);

    Optional<TrainedInProjection> findProjectedById(TrainedInId id);

    // ── By Physician ──────────────────────────────────────────────────────────
    List<TrainedIn> findById_PhysicianId(Integer physicianId);

    long countById_PhysicianId(Integer physicianId);

    // ── By Procedure ──────────────────────────────────────────────────────────
    List<TrainedIn> findById_TreatmentCode(Integer treatmentCode);

    long countById_TreatmentCode(Integer treatmentCode);

    // ── By Certification Date ─────────────────────────────────────────────────
    List<TrainedIn> findByCertificationDateBetween(LocalDateTime from, LocalDateTime to);

    // ── By Expiry ─────────────────────────────────────────────────────────────
    List<TrainedIn> findByCertificationExpiresBefore(LocalDateTime dateTime);

    // ── Existence Check ───────────────────────────────────────────────────────
    boolean existsById_PhysicianIdAndId_TreatmentCode(Integer physicianId, Integer treatmentCode);

    List<TrainedIn> findByCertificationExpiresAfterOrderByCertificationExpiresAsc(LocalDateTime now);

    List<TrainedIn> findById_PhysicianIdAndCertificationExpiresBefore(Integer physicianId, LocalDateTime now);

    List<TrainedIn> findByCertificationExpiresBetweenOrderByCertificationExpiresAsc(
            LocalDateTime now, LocalDateTime deadline);
}
