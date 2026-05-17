package com.hospital.repository;

import com.hospital.entity.Prescription;
import com.hospital.entity.PrescriptionId;
import com.hospital.projection.PrescriptionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "prescriptions")
public interface PrescriptionRepository extends JpaRepository<Prescription, PrescriptionId> {

    Page<PrescriptionProjection> findAllProjectedBy(Pageable pageable);

    Optional<PrescriptionProjection> findProjectedById(PrescriptionId id);

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

    List<Prescription> findAllByOrderByDateDesc();

    List<Prescription> findById_PatientSsnOrderByDateDesc(Integer patientSsn);
}
