package com.hospital.repository;

import com.hospital.entity.Appointment;
import com.hospital.projection.AppointmentProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    Page<AppointmentProjection> findAllProjectedBy(Pageable pageable);

    Optional<AppointmentProjection> findProjectedByAppointmentId(Integer appointmentId);

    List<Appointment> findByPatient_Ssn(Integer patientSsn);

    long countByPatient_Ssn(Integer patientSsn);

    List<Appointment> findByPhysician_EmployeeId(Integer physicianId);

    long countByPhysician_EmployeeId(Integer physicianId);

    List<Appointment> findByPrepNurse_EmployeeId(Integer nurseId);

    List<Appointment> findByPrepNurseIsNull();

    List<Appointment> findByExaminationRoom(String examinationRoom);

    List<Appointment> findByStartBetween(LocalDateTime from, LocalDateTime to);

    List<Appointment> findByPatient_SsnAndPhysician_EmployeeId(Integer patientSsn, Integer physicianId);

    List<Appointment> findByPhysician_EmployeeIdAndStartBetween(Integer physicianId, LocalDateTime from,
            LocalDateTime to);

    List<Appointment> findByPatient_SsnAndStartAfterOrderByStartAsc(Integer patientSsn, LocalDateTime now);

    List<Appointment> findAllByOrderByStartAsc();

    /**
     * True if the patient already has an appointment starting at the given time.
     */
    boolean existsByPatient_SsnAndStart(Integer patientSsn, LocalDateTime start);

    /** Same check but ignoring a specific appointment (used during updates). */
    boolean existsByPatient_SsnAndStartAndAppointmentIdNot(Integer patientSsn, LocalDateTime start,
            Integer appointmentId);
}
