package com.hospital.PatientTests;

import com.hospital.entity.Patient;
import com.hospital.entity.Physician;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.PhysicianRepository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    // ---------------------------------------------------
    // findByName(String name)
    // ---------------------------------------------------

    @Test
    void testFindByName_WhenPatientExists() {

        Physician physician = new Physician();
        physician.setEmployeeId(101);
        physician.setName("Dr Strange");
        physician.setPosition("Cardiologist");
        physician.setSsn(123456789);

        physicianRepository.save(physician);

        Patient patient = new Patient();
        patient.setSsn(1);
        patient.setName("John");
        patient.setAddress("Delhi");
        patient.setPhone("9999999999");
        patient.setInsuranceId(1001);
        patient.setPrimaryCarePhysician(physician);

        patientRepository.save(patient);

        Optional<Patient> result =
                patientRepository.findByName("John");

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getName());
    }

    @Test
    void testFindByName_WhenPatientDoesNotExist() {

        Optional<Patient> result =
                patientRepository.findByName("Unknown");

        assertTrue(result.isEmpty());
    }

    // ---------------------------------------------------
    // existsByName(String name)
    // ---------------------------------------------------

    @Test
    void testExistsByName_WhenPatientExists() {

        Physician physician = new Physician();
        physician.setEmployeeId(102);
        physician.setName("Dr House");
        physician.setPosition("Neurologist");
        physician.setSsn(987654321);

        physicianRepository.save(physician);

        Patient patient = new Patient();
        patient.setSsn(2);
        patient.setName("Rahul");
        patient.setAddress("Mumbai");
        patient.setPhone("8888888888");
        patient.setInsuranceId(1002);
        patient.setPrimaryCarePhysician(physician);

        patientRepository.save(patient);

        boolean result =
                patientRepository.existsByName("Rahul");

        assertTrue(result);
    }

    @Test
    void testExistsByName_WhenPatientDoesNotExist() {

        boolean result =
                patientRepository.existsByName("Unknown");

        assertFalse(result);
    }

    // ---------------------------------------------------
    // findByPrimaryCarePhysician_EmployeeId(Integer id)
    // ---------------------------------------------------

    @Test
    void testFindByPrimaryCarePhysicianEmployeeId_WhenPatientsExist() {

        Physician physician = new Physician();
        physician.setEmployeeId(201);
        physician.setName("Dr Strange");
        physician.setPosition("Cardiologist");
        physician.setSsn(111111111);

        physicianRepository.save(physician);

        Patient patient1 = new Patient(
                3,
                "John",
                "Delhi",
                "9999999999",
                1003,
                physician
        );

        Patient patient2 = new Patient(
                4,
                "Amit",
                "Noida",
                "7777777777",
                1004,
                physician
        );

        patientRepository.save(patient1);
        patientRepository.save(patient2);

        List<Patient> result =
                patientRepository
                        .findByPrimaryCarePhysician_EmployeeId(201);

        assertEquals(2, result.size());
    }

    @Test
    void testFindByPrimaryCarePhysicianEmployeeId_WhenNoPatientsExist() {

        Physician physician = new Physician();
        physician.setEmployeeId(202);
        physician.setName("Dr Watson");
        physician.setPosition("ENT");
        physician.setSsn(222222222);

        physicianRepository.save(physician);

        List<Patient> result =
                patientRepository
                        .findByPrimaryCarePhysician_EmployeeId(202);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByPrimaryCarePhysicianEmployeeId_WithMultiplePhysicians() {

        Physician physician1 = new Physician();
        physician1.setEmployeeId(301);
        physician1.setName("Dr Strange");
        physician1.setPosition("Cardiologist");
        physician1.setSsn(333333333);

        physicianRepository.save(physician1);

        Physician physician2 = new Physician();
        physician2.setEmployeeId(302);
        physician2.setName("Dr House");
        physician2.setPosition("Neurologist");
        physician2.setSsn(444444444);

        physicianRepository.save(physician2);

        Patient patient1 = new Patient(
                5,
                "John",
                "Delhi",
                "9999999999",
                1005,
                physician1
        );

        Patient patient2 = new Patient(
                6,
                "Amit",
                "Noida",
                "8888888888",
                1006,
                physician1
        );

        Patient patient3 = new Patient(
                7,
                "Rahul",
                "Mumbai",
                "7777777777",
                1007,
                physician2
        );

        patientRepository.save(patient1);
        patientRepository.save(patient2);
        patientRepository.save(patient3);

        List<Patient> result =
                patientRepository
                        .findByPrimaryCarePhysician_EmployeeId(301);

        assertEquals(2, result.size());

        for (Patient patient : result) {
            assertEquals(
                    301,
                    patient.getPrimaryCarePhysician().getEmployeeId()
            );
        }
    }

    @Test
    void testFindByPrimaryCarePhysicianEmployeeId_WhenPhysicianDoesNotExist() {

        List<Patient> result =
                patientRepository
                        .findByPrimaryCarePhysician_EmployeeId(999);

        assertTrue(result.isEmpty());
    }
}