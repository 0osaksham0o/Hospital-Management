package com.hospital.service;

import com.hospital.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    List<Appointment> getAll();
    Page<Appointment> getAll(Pageable pageable);
    Appointment getById(Integer id);
    boolean existsById(Integer id);
    Appointment save(Appointment appointment);
    void delete(Integer id);

    // By Patient
    List<Appointment> getByPatient(Integer patientSsn);
    long countByPatient(Integer patientSsn);

    // By Physician
    List<Appointment> getByPhysician(Integer physicianId);
    long countByPhysician(Integer physicianId);

    // By Nurse
    List<Appointment> getByPrepNurse(Integer nurseId);
    List<Appointment> getWithNoPrepNurse();

    // By Room
    List<Appointment> getByExaminationRoom(String room);

    // By Time
    List<Appointment> getByStartBetween(LocalDateTime from, LocalDateTime to);
    List<Appointment> getUpcomingForPatient(Integer patientSsn, LocalDateTime now);
    List<Appointment> getAllOrderedByStart();

    // Combined
    List<Appointment> getByPatientAndPhysician(Integer patientSsn, Integer physicianId);
    List<Appointment> getByPhysicianInPeriod(Integer physicianId, LocalDateTime from, LocalDateTime to);
}
