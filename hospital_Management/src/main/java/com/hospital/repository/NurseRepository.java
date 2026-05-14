package com.hospital.repository;

import com.hospital.entity.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Nurse entity.
 * POST, PUT, DELETE, PATCH handled by NurseController.
 */
@RepositoryRestResource(exported = false)
public interface NurseRepository extends JpaRepository<Nurse, Integer> {

    // ── By Name ───────────────────────────────────────────────────────────────

    /** Find nurse by exact name. */
    Optional<Nurse> findByName(String name);

    /** Find nurses whose name contains a keyword (case-insensitive). */
    List<Nurse> findByNameContainingIgnoreCase(String namePart);

    // ── By Position ───────────────────────────────────────────────────────────

    /** Find all nurses with a specific position. */
    List<Nurse> findByPosition(String position);


}
