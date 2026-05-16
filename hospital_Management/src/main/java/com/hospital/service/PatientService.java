package com.hospital.service;

import com.hospital.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    List<Patient> getAll();
    Page<Patient> getAll(Pageable pageable);
    Patient getById(Integer ssn);
    boolean existsById(Integer ssn);
    Patient save(Patient patient);
    void delete(Integer ssn);

    // By Name
    Optional<Patient> getByName(String name);
    List<Patient> searchByName(String namePart);
    boolean existsByName(String name);

    // By Contact
    Optional<Patient> getByPhone(String phone);
    List<Patient> getByAddress(String address);

    // By Insurance
    Optional<Patient> getByInsuranceId(Integer insuranceId);
    boolean existsByInsuranceId(Integer insuranceId);

    // By Physician
    List<Patient> getByPcp(Integer physicianId);
    long countByPcp(Integer physicianId);

    // Sorted
    List<Patient> getAllOrderedByName();
}
