package com.hospital.PrescriptionTest;

import com.hospital.entity.*;
import com.hospital.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PrescriptionRepositoryTest {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    @DisplayName("Should save and fetch prescription by composite id")
    void testSaveAndFindById() {

        Physician physician = new Physician();
        physician.setEmployeeId(1);
        physician.setName("Dr Strange");
        physicianRepository.save(physician);

        Patient patient = new Patient();
        patient.setSsn(101);
        patient.setName("Tony Stark");
        patientRepository.save(patient);

        Medication medication = new Medication();
        medication.setCode(1001);
        medication.setName("Paracetamol");
        medicationRepository.save(medication);

        Appointment appointment = new Appointment();
        appointment.setAppointmentId(5001);
        appointmentRepository.save(appointment);

        PrescriptionId prescriptionId =
                new PrescriptionId(1, 101, 1001);

        Prescription prescription = new Prescription(
                prescriptionId,
                physician,
                patient,
                medication,
                LocalDate.now(),
                appointment,
                "2 times a day"
        );

        prescriptionRepository.save(prescription);

        Optional<Prescription> saved =
                prescriptionRepository.findById(prescriptionId);

        assertThat(saved).isPresent();
        assertThat(saved.get().getDose())
                .isEqualTo("2 times a day");
    }

    @Test
    @DisplayName("Should find prescriptions by physician id")
    void testFindByPhysicianId() {

        Physician physician = new Physician();
        physician.setEmployeeId(2);
        physician.setName("Dr House");
        physicianRepository.save(physician);

        Patient patient = new Patient();
        patient.setSsn(102);
        patient.setName("Bruce Wayne");
        patientRepository.save(patient);

        Medication medication = new Medication();
        medication.setCode(1002);
        medication.setName("Ibuprofen");
        medicationRepository.save(medication);

        Prescription prescription = new Prescription(
                new PrescriptionId(2, 102, 1002),
                physician,
                patient,
                medication,
                LocalDate.now(),
                null,
                "Once daily"
        );

        prescriptionRepository.save(prescription);

        List<Prescription> prescriptions =
                prescriptionRepository.findById_PhysicianId(2);

        assertThat(prescriptions).hasSize(1);
    }

    @Test
    @DisplayName("Should count prescriptions by patient ssn")
    void testCountByPatientSsn() {

        Physician physician = new Physician();
        physician.setEmployeeId(3);
        physician.setName("Dr Who");
        physicianRepository.save(physician);

        Patient patient = new Patient();
        patient.setSsn(103);
        patient.setName("Peter Parker");
        patientRepository.save(patient);

        Medication medication = new Medication();
        medication.setCode(1003);
        medication.setName("Aspirin");
        medicationRepository.save(medication);

        Prescription prescription1 = new Prescription(
                new PrescriptionId(3, 103, 1003),
                physician,
                patient,
                medication,
                LocalDate.now(),
                null,
                "Morning"
        );

        Prescription prescription2 = new Prescription(
                new PrescriptionId(3, 103, 1004),
                physician,
                patient,
                medication,
                LocalDate.now(),
                null,
                "Night"
        );

        prescriptionRepository.save(prescription1);
        prescriptionRepository.save(prescription2);

        long count =
                prescriptionRepository.countById_PatientSsn(103);

        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Should find prescriptions between dates")
    void testFindByDateBetween() {

        Physician physician = new Physician();
        physician.setEmployeeId(4);
        physicianRepository.save(physician);

        Patient patient = new Patient();
        patient.setSsn(104);
        patientRepository.save(patient);

        Medication medication = new Medication();
        medication.setCode(1005);
        medicationRepository.save(medication);

        Prescription prescription = new Prescription(
                new PrescriptionId(4, 104, 1005),
                physician,
                patient,
                medication,
                LocalDate.of(2026, 5, 10),
                null,
                "After meal"
        );

        prescriptionRepository.save(prescription);

        List<Prescription> prescriptions =
                prescriptionRepository.findByDateBetween(
                        LocalDate.of(2026, 5, 1),
                        LocalDate.of(2026, 5, 20)
                );

        assertThat(prescriptions).isNotEmpty();
    }

    @Test
    @DisplayName("Should return prescriptions ordered by date desc")
    void testFindAllOrderByDateDesc() {

        List<Prescription> prescriptions =
                prescriptionRepository.findAllByOrderByDateDesc();

        assertThat(prescriptions).isNotNull();
    }
}