package com.hospital.repository;

import com.hospital.entity.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Spring Data REST exposes GET-only endpoints at /api/physicians
 * GET /api/physicians          – list all (paginated, size=5)
 * GET /api/physicians/{id}     – get by ID
 * POST, PUT, PATCH, DELETE     – handled by PhysicianController
 */
@RepositoryRestResource(path = "physicians")
public interface PhysicianRepository extends JpaRepository<Physician, Integer> {
}
