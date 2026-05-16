package com.hospital.service;

import com.hospital.entity.Medication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MedicationService {
    List<Medication> getAll();
    Page<Medication> getAll(Pageable pageable);
    Medication getById(Integer code);
    boolean existsById(Integer code);
    Medication save(Medication medication);
    void delete(Integer code);

    // By Name
    Optional<Medication> getByName(String name);
    List<Medication> searchByName(String keyword);
    boolean existsByName(String name);

    // By Brand
    List<Medication> getByBrand(String brand);
    List<Medication> searchByBrand(String keyword);
    List<String> getAllDistinctBrands();

    // By Description
    List<Medication> searchByDescription(String keyword);

    // Sorted
    List<Medication> getAllOrderedByName();
}
