package com.hospital.repository;

import com.hospital.entity.Procedure;
import com.hospital.projection.ProcedureProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Procedure entity.
 * POST, PUT, DELETE, PATCH handled by ProcedureController.
 */
@RepositoryRestResource(exported = false)
public interface ProcedureRepository extends JpaRepository<Procedure, Integer> {

    // ── Projection-based reads ─────────────────────────────────────────────
    Page<ProcedureProjection> findAllProjectedBy(Pageable pageable);
    Optional<ProcedureProjection> findProjectedByCode(Integer code);

    // ── By Name ───────────────────────────────────────────────────────────────

    /** Find procedure by exact name. */
    Optional<Procedure> findByName(String name);

    /** Find procedures whose name contains a keyword (case-insensitive). */
    List<Procedure> findByNameContainingIgnoreCase(String keyword);

    /** Check if a procedure with this name exists. */
    boolean existsByName(String name);

    // ── By Cost ───────────────────────────────────────────────────────────────

    /** Find procedures cheaper than a given cost. */
    List<Procedure> findByCostLessThan(Double maxCost);

    /** Find procedures more expensive than a given cost. */
    List<Procedure> findByCostGreaterThan(Double minCost);

    /** Find procedures within a cost range (inclusive). */
    List<Procedure> findByCostBetween(Double minCost, Double maxCost);

    List<Procedure> findAllByOrderByCostAsc();

    /** Find all procedures ordered by name ascending. */
    List<Procedure> findAllByOrderByNameAsc();
}
