package com.hospital.repository;

import com.hospital.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;
@RepositoryRestResource(exported = false)
public interface MedicationRepository extends JpaRepository<Medication, Integer> {
    Optional<Medication> findByName(String name);
    List<Medication> findByNameContainingIgnoreCase(String keyword);

    boolean existsByName(String name);

    List<Medication> findByBrand(String brand);

    List<Medication> findByBrandContainingIgnoreCase(String keyword);

    List<Medication> findByDescriptionContainingIgnoreCase(String keyword);

}
