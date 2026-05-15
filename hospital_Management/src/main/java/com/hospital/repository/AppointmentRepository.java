package com.hospital.repository;

import com.hospital.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Appointment entity.
 * POST, PUT, DELETE, PATCH handled by AppointmentController.
 */
@RepositoryRestResource(exported = false)
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {


    List<Appointment> findByPatient_Ssn(Integer patientSsn);
    long countByPatient_Ssn(Integer patientSsn);


    List<Appointment> findByPhysician_EmployeeId(Integer physicianId);
    long countByPhysician_EmployeeId(Integer physicianId);


    List<Appointment> findByPrepNurse_EmployeeId(Integer nurseId);
    List<Appointment> findByPrepNurseIsNull();


    List<Appointment> findByExaminationRoom(String examinationRoom);


    List<Appointment> findByStartBetween(LocalDateTime from, LocalDateTime to);


    List<Appointment> findByPatient_SsnAndPhysician_EmployeeId(Integer patientSsn, Integer physicianId);
    List<Appointment> findByPhysician_EmployeeIdAndStartBetween(Integer physicianId, LocalDateTime from, LocalDateTime to);


}
