package com.hospital.repository;

import com.hospital.entity.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "physicians")
public interface PhysicianRepository extends JpaRepository<Physician, Integer> {

    Optional<Physician> findByName(String name);

    List<Physician> findByNameContainingIgnoreCase(String namePart);

    List<Physician> findByPosition(String position);

    List<Physician> findByPositionContainingIgnoreCase(String keyword);

    Optional<Physician> findBySsn(Integer ssn);
    boolean existsBySsn(Integer ssn);

}
