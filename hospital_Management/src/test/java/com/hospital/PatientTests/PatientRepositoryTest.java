package com.hospital.PatientTests;

import com.hospital.entity.Patient;
import com.hospital.entity.Physician;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.PhysicianRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    private Physician savedPhysician;

    @BeforeEach
    void setup() {
        // Seed a physician then patients in H2
        savedPhysician = physicianRepository.save(
                new Physician(1, "Dr. House", "General", 100001));

        patientRepository.save(new Patient(
                10001, "John Smith", "42 Foobar Lane", "555-0256", 68476213, savedPhysician));
        patientRepository.save(new Patient(
                10002, "Jane Johnson", "15 Oak Street", "555-0101", 11223344, savedPhysician));
    }

    @Test
    @DisplayName("Find all patients - should not be empty after seeding")
    void testGetAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        assertFalse(patients.isEmpty());
        assertEquals(2, patients.size());
    }

    @Test
    @DisplayName("Find patient by exact name")
    void testFindExistingPatientByName() {
        Optional<Patient> result = patientRepository.findByName("John Smith");
        assertTrue(result.isPresent());
        assertEquals("John Smith", result.get().getName());
    }

    @Test
    @DisplayName("existsByName returns true for seeded patient")
    void testCheckExistingPatientName() {
        boolean exists = patientRepository.existsByName("John Smith");
        assertTrue(exists);
    }

    @Test
    @DisplayName("Find patients by primary care physician ID")
    void testFetchPatientsByPhysicianId() {
        List<Patient> patients =
                patientRepository.findByPrimaryCarePhysician_EmployeeId(savedPhysician.getEmployeeId());
        assertFalse(patients.isEmpty());
        patients.forEach(p -> assertEquals(savedPhysician.getEmployeeId(),
                p.getPrimaryCarePhysician().getEmployeeId()));
    }

    @Test
    @DisplayName("Find patients by partial name (case-insensitive)")
    void testFindPatientsByNameContainingIgnoreCase() {
        List<Patient> patients =
                patientRepository.findByNameContainingIgnoreCase("john");
        assertFalse(patients.isEmpty());
        patients.forEach(p ->
                assertTrue(p.getName().toLowerCase().contains("john")));
    }

    @Test
    @DisplayName("Find patient by phone number")
    void testFindPatientByPhone() {
        Optional<Patient> result = patientRepository.findByPhone("555-0256");
        assertTrue(result.isPresent());
        assertEquals("555-0256", result.get().getPhone());
    }

    @Test
    @DisplayName("Find patients by address")
    void testFindPatientsByAddress() {
        List<Patient> patients =
                patientRepository.findByAddress("42 Foobar Lane");
        assertFalse(patients.isEmpty());
        patients.forEach(p -> assertEquals("42 Foobar Lane", p.getAddress()));
    }

    @Test
    @DisplayName("Find patient by insurance ID")
    void testFindPatientByInsuranceId() {
        Optional<Patient> result = patientRepository.findByInsuranceId(68476213);
        assertTrue(result.isPresent());
        assertEquals(68476213, result.get().getInsuranceId());
    }

    @Test
    @DisplayName("existsByInsuranceId returns true for seeded patient")
    void testCheckExistingInsuranceId() {
        boolean exists = patientRepository.existsByInsuranceId(68476213);
        assertTrue(exists);
    }

    @Test
    @DisplayName("Count patients by physician ID")
    void testCountPatientsByPhysicianId() {
        long count = patientRepository.countByPrimaryCarePhysician_EmployeeId(
                savedPhysician.getEmployeeId());
        assertEquals(2, count);
    }

    @Test
    @DisplayName("Find non-existent patient returns empty")
    void testFindNonExistentPatient() {
        Optional<Patient> result = patientRepository.findByName("No Such Patient XYZ");
        assertFalse(result.isPresent());
    }
}