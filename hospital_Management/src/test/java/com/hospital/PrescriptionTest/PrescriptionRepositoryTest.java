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

    // ── Helpers ────────────────────────────────────────────────────────────────

    /** Saves a Physician with all required (NOT NULL) fields. */
    private Physician savePhysician(int id, String name, String position, int ssn) {
        Physician p = new Physician();
        p.setEmployeeId(id);
        p.setName(name);
        p.setPosition(position);
        p.setSsn(ssn);
        return physicianRepository.save(p);
    }

    /** Saves a Patient linked to the given PCP. */
    private Patient savePatient(int ssn, String name, String address,
                                String phone, int insuranceId, Physician pcp) {
        Patient p = new Patient();
        p.setSsn(ssn);
        p.setName(name);
        p.setAddress(address);
        p.setPhone(phone);
        p.setInsuranceId(insuranceId);
        p.setPrimaryCarePhysician(pcp);
        return patientRepository.save(p);
    }

    /** Saves a Medication with all required (NOT NULL) fields. */
    private Medication saveMedication(int code, String name) {
        Medication m = new Medication();
        m.setCode(code);
        m.setName(name);
        m.setBrand("TestBrand");
        m.setDescription("Test description");
        return medicationRepository.save(m);
    }

    // ── Tests ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should save and fetch prescription by composite id")
    void testSaveAndFindById() {

        Physician physician = savePhysician(7001, "Dr Strange", "Surgeon", 111111111);
        Patient patient = savePatient(7101, "Tony Stark", "Malibu Point", "555-0001", 10000001, physician);
        Medication medication = saveMedication(7001, "Paracetamol");

        PrescriptionId prescriptionId = new PrescriptionId(7001, 7101, 7001);

        Prescription prescription = new Prescription(
                prescriptionId, physician, patient, medication,
                LocalDate.now(), null, "2 times a day"
        );
        prescriptionRepository.save(prescription);

        Optional<Prescription> saved = prescriptionRepository.findById(prescriptionId);
        assertThat(saved).isPresent();
        assertThat(saved.get().getDose()).isEqualTo("2 times a day");
    }

    @Test
    @DisplayName("Should find prescriptions by physician id")
    void testFindByPhysicianId() {

        Physician physician = savePhysician(7002, "Dr House", "Diagnostician", 222222222);
        Patient patient = savePatient(7102, "Bruce Wayne", "Gotham City", "555-0002", 10000002, physician);
        Medication medication = saveMedication(7002, "Ibuprofen");

        Prescription prescription = new Prescription(
                new PrescriptionId(7002, 7102, 7002),
                physician, patient, medication,
                LocalDate.now(), null, "Once daily"
        );
        prescriptionRepository.save(prescription);

        List<Prescription> prescriptions = prescriptionRepository.findById_PhysicianId(7002);
        assertThat(prescriptions).hasSize(1);
    }

    @Test
    @DisplayName("Should count prescriptions by patient ssn")
    void testCountByPatientSsn() {

        Physician physician = savePhysician(7003, "Dr Who", "Generalist", 333333333);
        Patient patient = savePatient(7103, "Peter Parker", "Queens, NY", "555-0003", 10000003, physician);
        Medication medication1 = saveMedication(7003, "Aspirin");
        Medication medication2 = saveMedication(7004, "Amoxicillin");

        prescriptionRepository.save(new Prescription(
                new PrescriptionId(7003, 7103, 7003),
                physician, patient, medication1,
                LocalDate.now(), null, "Morning"
        ));
        prescriptionRepository.save(new Prescription(
                new PrescriptionId(7003, 7103, 7004),
                physician, patient, medication2,
                LocalDate.now(), null, "Night"
        ));

        long count = prescriptionRepository.countById_PatientSsn(7103);
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Should find prescriptions between dates")
    void testFindByDateBetween() {

        Physician physician = savePhysician(7004, "Dr Grey", "Surgeon", 444444444);
        Patient patient = savePatient(7104, "Clint Barton", "New York", "555-0004", 10000004, physician);
        Medication medication = saveMedication(7005, "Amoxicillin");

        prescriptionRepository.save(new Prescription(
                new PrescriptionId(7004, 7104, 7005),
                physician, patient, medication,
                LocalDate.of(2026, 5, 10), null, "After meal"
        ));

        List<Prescription> prescriptions = prescriptionRepository.findByDateBetween(
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 20)
        );
        assertThat(prescriptions).isNotEmpty();
    }

    @Test
    @DisplayName("Should return prescriptions ordered by date desc")
    void testFindAllOrderByDateDesc() {

        List<Prescription> prescriptions = prescriptionRepository.findAllByOrderByDateDesc();
        assertThat(prescriptions).isNotNull();
    }
}