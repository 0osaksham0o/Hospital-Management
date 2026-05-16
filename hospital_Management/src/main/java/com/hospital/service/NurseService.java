package com.hospital.service;

import com.hospital.entity.Nurse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NurseService {
    List<Nurse> getAll();
    Page<Nurse> getAll(Pageable pageable);
    Nurse getById(Integer id);
    boolean existsById(Integer id);
    Nurse save(Nurse nurse);
    void delete(Integer id);

    // By Name
    Optional<Nurse> getByName(String name);
    List<Nurse> searchByName(String namePart);

    // By Position
    List<Nurse> getByPosition(String position);

    // By Registration
    List<Nurse> getByRegistered(Boolean registered);
    long countByRegistered(Boolean registered);

    // By SSN
    Optional<Nurse> getBySsn(Integer ssn);
    boolean existsBySsn(Integer ssn);

    // Sorted
    List<Nurse> getAllRegisteredOrderedByName();
}
