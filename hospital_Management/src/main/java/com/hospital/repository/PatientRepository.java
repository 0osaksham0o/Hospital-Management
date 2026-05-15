package com.hospital.repository;

import com.hospital.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;


@RepositoryRestResource(exported = false)
public interface PatientRepository extends JpaRepository<Patient, Integer> {


    Optional<Patient> findByName(String name);
    List<Patient> findByNameContainingIgnoreCase(String namePart);
    boolean existsByName(String name);


    Optional<Patient> findByPhone(String phone);
    List<Patient> findByAddress(String address);


    Optional<Patient> findByInsuranceId(Integer insuranceId);
    boolean existsByInsuranceId(Integer insuranceId);


    List<Patient> findByPrimaryCarePhysician_EmployeeId(Integer physicianId);
    long countByPrimaryCarePhysician_EmployeeId(Integer physicianId);


}