package com.hospital.AppointmentsTest;

import com.hospital.entity.Appointment;
import com.hospital.entity.Patient;
import com.hospital.entity.Physician;
import com.hospital.repository.AppointmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TestEntityManager entityManager;


    // ==========================================
    // Helper Methods
    // ==========================================

    private Physician createPhysician(Integer id) {

        Physician physician = new Physician();

        physician.setEmployeeId(id);
        physician.setName("Doctor " + id);
        physician.setPosition("Cardiologist");
        physician.setSsn(1000 + id);

        return entityManager.persist(physician);
    }

    private Patient createPatient(Integer ssn, Physician physician) {

        Patient patient = new Patient();

        patient.setSsn(ssn);
        patient.setName("Patient " + ssn);
        patient.setAddress("Punjab");
        patient.setPhone("9876543210");
        patient.setInsuranceId(5000 + ssn);

        patient.setPrimaryCarePhysician(physician);

        return entityManager.persist(patient);
    }


    // ==========================================
    // TEST CASES
    // ==========================================

    @Test
    @DisplayName("findByPatient_Ssn")
    void testFindByPatientSsn() {

        Physician physician = createPhysician(201);

        Patient patient = createPatient(101, physician);

        Appointment appointment = new Appointment(
                1,
                patient,
                null,
                physician,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                "Room-1"
        );

        entityManager.persist(appointment);
        entityManager.flush();

        List<Appointment> result =
                appointmentRepository.findByPatient_Ssn(101);

        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getPatient().getSsn());
    }


    @Test
    @DisplayName("countByPatient_Ssn")
    void testCountByPatientSsn() {

        Physician physician = createPhysician(202);

        Patient patient = createPatient(102, physician);

        Appointment a1 = new Appointment(
                2,
                patient,
                null,
                physician,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                "Room-2"
        );

        Appointment a2 = new Appointment(
                3,
                patient,
                null,
                physician,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "Room-3"
        );

        entityManager.persist(a1);
        entityManager.persist(a2);

        entityManager.flush();

        long count =
                appointmentRepository.countByPatient_Ssn(102);

        assertEquals(2, count);
    }


    @Test
    @DisplayName("findByPhysician_EmployeeId")
    void testFindByPhysicianEmployeeId() {

        Physician physician = createPhysician(203);

        Patient patient = createPatient(103, physician);

        Appointment appointment = new Appointment(
                4,
                patient,
                null,
                physician,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                "Room-4"
        );

        entityManager.persist(appointment);

        entityManager.flush();

        List<Appointment> result =
                appointmentRepository.findByPhysician_EmployeeId(203);

        assertEquals(1, result.size());
    }


    @Test
    @DisplayName("findByPrepNurseIsNull")
    void testFindByPrepNurseIsNull() {

        Physician physician = createPhysician(204);

        Patient patient = createPatient(104, physician);

        Appointment appointment = new Appointment(
                5,
                patient,
                null,
                physician,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                "Room-5"
        );

        entityManager.persist(appointment);

        entityManager.flush();

        List<Appointment> result =
                appointmentRepository.findByPrepNurseIsNull();

        assertFalse(result.isEmpty());
    }


    @Test
    @DisplayName("findByExaminationRoom")
    void testFindByExaminationRoom() {

        Physician physician = createPhysician(205);

        Patient patient = createPatient(105, physician);

        Appointment appointment = new Appointment(
                6,
                patient,
                null,
                physician,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                "ICU-1"
        );

        entityManager.persist(appointment);

        entityManager.flush();

        List<Appointment> result =
                appointmentRepository.findByExaminationRoom("ICU-1");

        assertEquals(1, result.size());
        assertEquals(
                "ICU-1",
                result.get(0).getExaminationRoom()
        );
    }


    @Test
    @DisplayName("findByStartBetween")
    void testFindByStartBetween() {

        Physician physician = createPhysician(206);

        Patient patient = createPatient(106, physician);

        LocalDateTime startTime = LocalDateTime.now();

        Appointment appointment = new Appointment(
                7,
                patient,
                null,
                physician,
                startTime,
                startTime.plusHours(1),
                "Room-7"
        );

        entityManager.persist(appointment);

        entityManager.flush();

        List<Appointment> result =
                appointmentRepository.findByStartBetween(
                        startTime.minusDays(1),
                        startTime.plusDays(1)
                );

        assertEquals(1, result.size());
    }


    @Test
    @DisplayName("findByPatient_SsnAndPhysician_EmployeeId")
    void testFindByPatientAndPhysician() {

        Physician physician = createPhysician(207);

        Patient patient = createPatient(107, physician);

        Appointment appointment = new Appointment(
                8,
                patient,
                null,
                physician,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                "Room-8"
        );

        entityManager.persist(appointment);

        entityManager.flush();

        List<Appointment> result =
                appointmentRepository
                        .findByPatient_SsnAndPhysician_EmployeeId(
                                107,
                                207
                        );

        assertEquals(1, result.size());
    }


    @Test
    @DisplayName("findByPhysician_EmployeeIdAndStartBetween")
    void testFindByPhysicianAndStartBetween() {

        Physician physician = createPhysician(208);

        Patient patient = createPatient(108, physician);

        LocalDateTime start = LocalDateTime.now();

        Appointment appointment = new Appointment(
                9,
                patient,
                null,
                physician,
                start,
                start.plusHours(1),
                "Room-9"
        );

        entityManager.persist(appointment);

        entityManager.flush();

        List<Appointment> result =
                appointmentRepository
                        .findByPhysician_EmployeeIdAndStartBetween(
                                208,
                                start.minusHours(1),
                                start.plusHours(2)
                        );

        assertEquals(1, result.size());
    }
}