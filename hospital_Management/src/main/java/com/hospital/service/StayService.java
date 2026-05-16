package com.hospital.service;

import com.hospital.entity.Stay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface StayService {
    List<Stay> getAll();
    Page<Stay> getAll(Pageable pageable);
    Stay getById(Integer stayId);
    boolean existsById(Integer stayId);
    Stay save(Stay stay);
    void delete(Integer stayId);

    // By Patient
    List<Stay> getByPatient(Integer patientSsn);
    long countByPatient(Integer patientSsn);

    // By Room
    List<Stay> getByRoom(Integer roomNumber);
    long countByRoom(Integer roomNumber);

    // By Date
    List<Stay> getByStartBetween(LocalDateTime from, LocalDateTime to);
    List<Stay> getByEndBetween(LocalDateTime from, LocalDateTime to);
    List<Stay> getCurrentStays(LocalDateTime now);

    // Combined
    List<Stay> getByPatientAndRoom(Integer patientSsn, Integer roomNumber);

    // Sorted
    List<Stay> getAllOrderedByStartDesc();
}
