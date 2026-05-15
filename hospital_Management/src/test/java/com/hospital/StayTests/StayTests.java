package com.hospital.repository;

import com.hospital.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private NurseRepository nurseRepository;

    @Test
    @DisplayName("Save Appointment Test")
    void saveAppointmentTest() {

        Physician physician = new Physician();
        physician.setEmployeeId(1);
        physician.setName("Dr John");
        physician.setPosition("Cardiologist");
        physician.setSsn(1111);

        physicianRepository.save(physician);

        Nurse nurse = new Nurse();
        nurse.setEmployeeId(2);
        nurse.setName("Nancy");
        nurse.setPosition("Senior Nurse");
        nurse.setRegistered(true);
        nurse.setSsn(2222);

        nurseRepository.save(nurse);

        Patient patient = new Patient();
        patient.setSsn(1001);
        patient.setName("Harshit");
        patient.setAddress("Punjab");
        patient.setPhone("9999999999");
        patient.setInsuranceId(5001);
        patient.setPrimaryCarePhysician(physician);

        patientRepository.save(patient);

        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1);
        appointment.setPatient(patient);
        appointment.setPhysician(physician);
        appointment.setPrepNurse(nurse);
        appointment.setStart(LocalDateTime.now());
        appointment.setEnd(LocalDateTime.now().plusHours(1));
        appointment.setExaminationRoom("Room A");

        Appointment saved = appointmentRepository.save(appointment);

        assertThat(saved).isNotNull();
        assertThat(saved.getAppointmentId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Find Appointment By ID")
    void findByIdTest() {

        Physician physician = new Physician();
        physician.setEmployeeId(11);
        physician.setName("Dr Smith");
        physician.setPosition("Neurologist");
        physician.setSsn(1112);

        physicianRepository.save(physician);

        Patient patient = new Patient();
        patient.setSsn(2001);
        patient.setName("Rahul");
        patient.setAddress("Delhi");
        patient.setPhone("8888888888");
        patient.setInsuranceId(9001);
        patient.setPrimaryCarePhysician(physician);

        patientRepository.save(patient);

        Appointment appointment = new Appointment();
        appointment.setAppointmentId(2);
        appointment.setPatient(patient);
        appointment.setPhysician(physician);
        appointment.setStart(LocalDateTime.now());
        appointment.setEnd(LocalDateTime.now().plusHours(2));
        appointment.setExaminationRoom("Room B");

        appointmentRepository.save(appointment);

        Appointment found =
                appointmentRepository.findById(2).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getAppointmentId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Find All Appointments")
    void findAllTest() {

        List<Appointment> appointments = appointmentRepository.findAll();

        assertThat(appointments).isNotNull();
    }

    @Test
    @DisplayName("Delete Appointment Test")
    void deleteAppointmentTest() {

        Physician physician = new Physician();
        physician.setEmployeeId(21);
        physician.setName("Dr Watson");
        physician.setPosition("ENT");
        physician.setSsn(3333);

        physicianRepository.save(physician);

        Patient patient = new Patient();
        patient.setSsn(3001);
        patient.setName("Aman");
        patient.setAddress("Chandigarh");
        patient.setPhone("7777777777");
        patient.setInsuranceId(7001);
        patient.setPrimaryCarePhysician(physician);

        patientRepository.save(patient);

        Appointment appointment = new Appointment();
        appointment.setAppointmentId(3);
        appointment.setPatient(patient);
        appointment.setPhysician(physician);
        appointment.setStart(LocalDateTime.now());
        appointment.setEnd(LocalDateTime.now().plusHours(1));
        appointment.setExaminationRoom("Room C");

        appointmentRepository.save(appointment);

        appointmentRepository.deleteById(3);

        Appointment deleted =
                appointmentRepository.findById(3).orElse(null);

        assertThat(deleted).isNull();
    }
}