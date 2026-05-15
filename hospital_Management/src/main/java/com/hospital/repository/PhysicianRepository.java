package com.hospital.repository;

import com.hospital.entity.Physician;
import com.hospital.projection.PhysicianProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "physicians")
public interface PhysicianRepository extends JpaRepository<Physician, Integer> {
    Page<PhysicianProjection> findAllProjectedBy(Pageable pageable);
    Optional<PhysicianProjection> findProjectedByEmployeeId(Integer employeeId);
    Page<PhysicianProjection> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Physician> findByName(String name);
    List<Physician> findByNameContainingIgnoreCase(String namePart);
    List<Physician> findByPosition(String position);
    List<Physician> findByPositionContainingIgnoreCase(String keyword);
    Optional<Physician> findBySsn(Integer ssn);
    boolean existsBySsn(Integer ssn);
    List<Physician> findAllByOrderByNameAsc();
}
