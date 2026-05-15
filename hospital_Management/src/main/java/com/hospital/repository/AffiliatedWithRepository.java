package com.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.hospital.entity.AffiliatedWith;
import com.hospital.entity.AffiliatedWithId;


@RepositoryRestResource(path = "affiliatedwith")
public interface AffiliatedWithRepository extends JpaRepository<AffiliatedWith, AffiliatedWithId> {

    // ── Projection-based reads ─────────────────────────────────────────────
    Page<AffiliatedWithProjection> findAllProjectedBy(Pageable pageable);
    Optional<AffiliatedWithProjection> findProjectedById(AffiliatedWithId id);

    // ── By Physician ──────────────────────────────────────────────────────────

    /** Find all departments a physician is affiliated with. */
    List<AffiliatedWith> findById_PhysicianId(Integer physicianId);

    /** Count how many departments a physician is affiliated with. */
    long countById_PhysicianId(Integer physicianId);

    // ── By Department ─────────────────────────────────────────────────────────

    /** Find all physicians affiliated with a department. */
    List<AffiliatedWith> findById_DepartmentId(Integer departmentId);

    /** Count physicians affiliated with a department. */
    long countById_DepartmentId(Integer departmentId);

    // ── By Primary Affiliation ────────────────────────────────────────────────

    /** Find physicians with primary affiliation in a department. */
    List<AffiliatedWith> findById_DepartmentIdAndPrimaryAffiliation(Integer departmentId, Boolean primary);

    /** Find all primary affiliations for a physician. */
    List<AffiliatedWith> findById_PhysicianIdAndPrimaryAffiliation(Integer physicianId, Boolean primary);

    /** Find all primary-affiliated records across all departments. */
    List<AffiliatedWith> findByPrimaryAffiliation(Boolean primary);

    // ── Existence Checks ──────────────────────────────────────────────────────

    /** Check if a physician is affiliated with a department. */
    boolean existsById_PhysicianIdAndId_DepartmentId(Integer physicianId, Integer departmentId);

}
