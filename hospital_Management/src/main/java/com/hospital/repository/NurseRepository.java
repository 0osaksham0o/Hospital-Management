package com.hospital.repository;

import com.hospital.entity.Nurse;
import com.hospital.projection.NurseProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Nurse entity.
 * POST, PUT, DELETE, PATCH handled by NurseController.
 */
@RepositoryRestResource(exported = false)
public interface NurseRepository extends JpaRepository<Nurse, Integer> {

    // ── Projection-based reads ─────────────────────────────────────────────
    Page<NurseProjection> findAllProjectedBy(Pageable pageable);
    Optional<NurseProjection> findProjectedByEmployeeId(Integer employeeId);

    // ── Paginated search by name ───────────────────────────────────────────
    Page<NurseProjection> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // ── By Name ───────────────────────────────────────────────────────────────

    /** Find nurse by exact name. */
    Optional<Nurse> findByName(String name);

    /** Find nurses whose name contains a keyword (case-insensitive). */
    List<Nurse> findByNameContainingIgnoreCase(String namePart);

    // ── By Position ───────────────────────────────────────────────────────────

    /** Find all nurses with a specific position. */
    List<Nurse> findByPosition(String position);

    // ── By Registration ───────────────────────────────────────────────────────

    /** Find all registered (or unregistered) nurses. */
    List<Nurse> findByRegistered(Boolean registered);

    /** Count registered nurses. */
    long countByRegistered(Boolean registered);

    // ── By SSN ────────────────────────────────────────────────────────────────

    /** Find nurse by SSN. */
    Optional<Nurse> findBySsn(Integer ssn);

    /** Check if a nurse SSN exists. */
    boolean existsBySsn(Integer ssn);

    /** Find all registered nurses sorted by name. */
    List<Nurse> findByRegisteredTrueOrderByNameAsc();
}
