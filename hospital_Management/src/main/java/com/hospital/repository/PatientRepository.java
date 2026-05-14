package com.hospital.repository;

import com.hospital.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;


@RepositoryRestResource(exported = false)
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    //By Name
    Optional<Patient> findByName(String name);
    boolean existsByName(String name);

    List<Patient> findByPrimaryCarePhysician_EmployeeId(Integer physicianId);



}