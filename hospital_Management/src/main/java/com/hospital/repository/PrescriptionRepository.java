package com.hospital.repository;

import com.hospital.entity.Prescription;
import com.hospital.entity.PrescriptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.List;

@RepositoryRestResource(path = "prescriptions")
public interface PrescriptionRepository extends JpaRepository<Prescription, PrescriptionId> {

    List<Prescription> findById_PhysicianId(Integer physicianId);

    long countById_PhysicianId(Integer physicianId);

    List<Prescription> findById_PatientSsn(Integer patientSsn);

    long countById_PatientSsn(Integer patientSsn);

    List<Prescription> findById_MedicationCode(Integer medicationCode);

    List<Prescription> findByAppointment_AppointmentId(Integer appointmentId);

    List<Prescription> findByDate(LocalDate date);

    List<Prescription> findByDateBetween(LocalDate from, LocalDate to);

    List<Prescription> findByDateAfter(LocalDate date);

    List<Prescription> findById_PatientSsnAndId_PhysicianId(Integer patientSsn, Integer physicianId);

    List<Prescription> findById_PatientSsnAndId_MedicationCode(Integer patientSsn, Integer medicationCode);

}
