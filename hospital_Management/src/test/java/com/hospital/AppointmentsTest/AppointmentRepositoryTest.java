package com.hospital.AppointmentsTest;

import com.hospital.entity.Appointment;
import com.hospital.entity.Nurse;
import com.hospital.entity.Patient;
import com.hospital.entity.Physician;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.NurseRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.PhysicianRepository;
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
    @DisplayName("Save and find Appointment by id")
    void saveAndFindByIdTest() {
        Appointment appointment = createAndSaveAppointment();

        Appointment found = appointmentRepository
                .findById(appointment.getAppointmentId())
                .orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getAppointmentId()).isEqualTo(1);
        assertThat(found.getPatient().getSsn()).isEqualTo(1001);
        assertThat(found.getPhysician().getEmployeeId()).isEqualTo(2001);
        assertThat(found.getPrepNurse().getEmployeeId()).isEqualTo(3001);
    }

    @Test
    @DisplayName("Find Appointment by patient, physician, nurse, and room")
    void findByPatientPhysicianNurseAndRoomTest() {
        createAndSaveAppointment();

        List<Appointment> byPatient = appointmentRepository.findByPatient_Ssn(1001);
        List<Appointment> byPhysician = appointmentRepository.findByPhysician_EmployeeId(2001);
        List<Appointment> byNurse = appointmentRepository.findByPrepNurse_EmployeeId(3001);
        List<Appointment> byRoom = appointmentRepository.findByExaminationRoom("Room A");

        assertThat(byPatient).hasSize(1);
        assertThat(appointmentRepository.countByPatient_Ssn(1001)).isEqualTo(1);
        assertThat(byPhysician).hasSize(1);
        assertThat(appointmentRepository.countByPhysician_EmployeeId(2001)).isEqualTo(1);
        assertThat(byNurse).hasSize(1);
        assertThat(byRoom).hasSize(1);
    }

    @Test
    @DisplayName("Find Appointment by start date range")
    void findByStartBetweenTest() {
        Appointment appointment = createAndSaveAppointment();

        List<Appointment> appointments = appointmentRepository.findByStartBetween(
                appointment.getStart().minusMinutes(30),
                appointment.getStart().plusMinutes(30)
        );

        assertThat(appointments).hasSize(1);
        assertThat(appointments.get(0).getAppointmentId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Find Appointment by patient and physician")
    void findByPatientAndPhysicianTest() {
        createAndSaveAppointment();

        List<Appointment> appointments =
                appointmentRepository.findByPatient_SsnAndPhysician_EmployeeId(1001, 2001);

        assertThat(appointments).hasSize(1);
        assertThat(appointments.get(0).getPatient().getSsn()).isEqualTo(1001);
        assertThat(appointments.get(0).getPhysician().getEmployeeId()).isEqualTo(2001);
    }

    @Test
    @DisplayName("Find Appointment by physician and start date range")
    void findByPhysicianAndStartBetweenTest() {
        Appointment appointment = createAndSaveAppointment();

        List<Appointment> appointments =
                appointmentRepository.findByPhysician_EmployeeIdAndStartBetween(
                        2001,
                        appointment.getStart().minusMinutes(30),
                        appointment.getStart().plusMinutes(30)
                );

        assertThat(appointments).hasSize(1);
        assertThat(appointments.get(0).getPhysician().getEmployeeId()).isEqualTo(2001);
    }

    @Test
    @DisplayName("Find Appointment with no prep nurse")
    void findByPrepNurseIsNullTest() {
        Physician physician = createPhysician(2002, "Dr Null Nurse", "General", 9002);
        Patient patient = createPatient(1002, "Patient Null Nurse", physician);
        Appointment appointment = new Appointment(
                2,
                patient,
                null,
                physician,
                LocalDateTime.of(2026, 5, 15, 12, 0),
                LocalDateTime.of(2026, 5, 15, 12, 30),
                "Room B"
        );
        appointmentRepository.saveAndFlush(appointment);

        List<Appointment> appointments = appointmentRepository.findByPrepNurseIsNull();

        assertThat(appointments).extracting(Appointment::getAppointmentId).contains(2);
    }

    private Appointment createAndSaveAppointment() {
        Physician physician = createPhysician(2001, "Dr John", "Cardiologist", 9001);
        Nurse nurse = nurseRepository.save(new Nurse(3001, "Nurse Nancy", "Senior Nurse", true, 8001));
        Patient patient = createPatient(1001, "Test Patient", physician);

        Appointment appointment = new Appointment(
                1,
                patient,
                nurse,
                physician,
                LocalDateTime.of(2026, 5, 15, 10, 0),
                LocalDateTime.of(2026, 5, 15, 10, 30),
                "Room A"
        );

        return appointmentRepository.saveAndFlush(appointment);
    }

    private Physician createPhysician(Integer employeeId, String name, String position, Integer ssn) {
        return physicianRepository.save(new Physician(employeeId, name, position, ssn));
    }

    private Patient createPatient(Integer ssn, String name, Physician primaryCarePhysician) {
        return patientRepository.save(new Patient(
                ssn,
                name,
                "Test Address",
                "9999999999",
                ssn + 5000,
                primaryCarePhysician
        ));
    }
}
