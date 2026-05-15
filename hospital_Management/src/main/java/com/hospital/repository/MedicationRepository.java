package com.hospital.repository;

import com.hospital.entity.Medication;
import com.hospital.projection.MedicationProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;


@RepositoryRestResource(exported = false)
public interface MedicationRepository extends JpaRepository<Medication, Integer> {


    Page<MedicationProjection> findAllProjectedBy(Pageable pageable);
    Optional<MedicationProjection> findProjectedByCode(Integer code);



    Optional<Medication> findByName(String name);


    List<Medication> findByNameContainingIgnoreCase(String keyword);

    boolean existsByName(String name);


    List<Medication> findByBrand(String brand);


    List<Medication> findByBrandContainingIgnoreCase(String keyword);




    List<Medication> findByDescriptionContainingIgnoreCase(String keyword);


    List<Medication> findAllByOrderByNameAsc();
}
