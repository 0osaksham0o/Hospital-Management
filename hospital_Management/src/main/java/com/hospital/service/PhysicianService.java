package com.hospital.service;

import com.hospital.entity.Physician;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PhysicianService {
    List<Physician> getAll();
    Page<Physician> getAll(Pageable pageable);
    Physician getById(Integer id);
    boolean existsById(Integer id);
    Physician save(Physician physician);
    void delete(Integer id);

    // By Name
    Optional<Physician> getByName(String name);
    List<Physician> searchByName(String namePart);

    // By Position
    List<Physician> getByPosition(String position);
    List<Physician> searchByPosition(String keyword);

    // By SSN
    Optional<Physician> getBySsn(Integer ssn);
    boolean existsBySsn(Integer ssn);

    // Queries
    List<Physician> getAllOrderedByName();
    List<Physician> getDepartmentHeads();
}
