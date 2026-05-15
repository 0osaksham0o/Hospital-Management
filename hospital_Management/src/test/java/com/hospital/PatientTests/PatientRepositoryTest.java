package com.hospital.PatientTests;

import com.hospital.entity.Patient;
import com.hospital.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void testGetAllPatientsFromDatabase() {
        List<Patient> patients = patientRepository.findAll();

        assertFalse(patients.isEmpty());

        patients.forEach(patient -> {
            System.out.println("SSN: " + patient.getSsn());
            System.out.println("Name: " + patient.getName());
            System.out.println("Address: " + patient.getAddress());
            System.out.println("Phone: " + patient.getPhone());
            System.out.println("Insurance ID: " + patient.getInsuranceId());

            if (patient.getPrimaryCarePhysician() != null) {
                System.out.println("Physician: "
                        + patient.getPrimaryCarePhysician().getName());
            }

            System.out.println("----------------------");
        });
    }

    @Test
    void testFindExistingPatientByNameFromDatabase() {
        Optional<Patient> result = patientRepository.findByName("John Smith");

        assertTrue(result.isPresent());
        assertEquals("John Smith", result.get().getName());
    }

    @Test
    void testCheckExistingPatientNameFromDatabase() {
        boolean exists = patientRepository.existsByName("John Smith");

        assertTrue(exists);
    }

    @Test
    void testFetchPatientsByExistingPhysicianIdFromDatabase() {
        List<Patient> patients =
                patientRepository.findByPrimaryCarePhysician_EmployeeId(1);

        assertFalse(patients.isEmpty());

        patients.forEach(patient ->
                assertEquals(1, patient.getPrimaryCarePhysician().getEmployeeId())
        );
    }

    @Test
    void testFindPatientsByNameContainingIgnoreCaseFromDatabase() {
        List<Patient> patients =
                patientRepository.findByNameContainingIgnoreCase("john");

        assertFalse(patients.isEmpty());

        patients.forEach(patient ->
                assertTrue(patient.getName().toLowerCase().contains("john"))
        );
    }

    @Test
    void testFindExistingPatientByPhoneFromDatabase() {
        Optional<Patient> result = patientRepository.findByPhone("555-0256");

        assertTrue(result.isPresent());
        assertEquals("555-0256", result.get().getPhone());
    }

    @Test
    void testFindPatientsByAddressFromDatabase() {
        List<Patient> patients =
                patientRepository.findByAddress("42 Foobar Lane");

        assertFalse(patients.isEmpty());

        patients.forEach(patient ->
                assertEquals("42 Foobar Lane", patient.getAddress())
        );
    }

    @Test
    void testFindExistingPatientByInsuranceIdFromDatabase() {
        Optional<Patient> result =
                patientRepository.findByInsuranceId(68476213);

        assertTrue(result.isPresent());
        assertEquals(68476213, result.get().getInsuranceId());
    }

    @Test
    void testCheckExistingInsuranceIdFromDatabase() {
        boolean exists =
                patientRepository.existsByInsuranceId(68476213);

        assertTrue(exists);
    }

    @Test
    void testCountPatientsByPhysicianIdFromDatabase() {
        long count =
                patientRepository.countByPrimaryCarePhysician_EmployeeId(1);

        assertTrue(count > 0);
    }
}