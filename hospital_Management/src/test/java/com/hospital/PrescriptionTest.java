package com.hospital;

import com.hospital.entity.*;
import com.hospital.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PrescriptionTest {

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

    private Physician createPhysician(Integer id, String name) {
        return new Physician(id, name, "Position", 100000000 + id);
    }

    private Patient createPatient(Integer ssn, Physician pcp) {
        return new Patient(ssn, "Patient " + ssn, "Address", "Phone", 2000 + ssn, pcp);
    }

    private Medication createMedication(Integer code, String name) {
        return new Medication(code, name, "Brand", "Description");
    }

    private Appointment createAppointment(Integer id, Patient patient, Physician physician) {
        return new Appointment(id, patient, null, physician, LocalDateTime.now().minusHours(1), LocalDateTime.now(), "Room 1");
    }

    @Test
    void testSaveAndQueryPrescription() {
        Physician phy = physicianRepository.save(createPhysician(501, "Dr Test"));
        Medication med = medicationRepository.save(createMedication(301, "MedTest"));
        Patient pat = patientRepository.save(createPatient(401, phy));
        Appointment appt = appointmentRepository.save(createAppointment(701, pat, phy));

        PrescriptionId pid = new PrescriptionId(phy.getEmployeeId(), pat.getSsn(), med.getCode());
        LocalDate today = LocalDate.now();
        Prescription pres = new Prescription(pid, phy, pat, med, today, appt, "1 tablet");

        prescriptionRepository.save(pres);

        List<Prescription> byPhys = prescriptionRepository.findById_PhysicianId(phy.getEmployeeId());
        assertEquals(1, byPhys.size());

        long countByPhys = prescriptionRepository.countById_PhysicianId(phy.getEmployeeId());
        assertEquals(1, countByPhys);

        List<Prescription> byPatientAndMed = prescriptionRepository.findById_PatientSsnAndId_MedicationCode(pat.getSsn(), med.getCode());
        assertEquals(1, byPatientAndMed.size());

        List<Prescription> byAppointment = prescriptionRepository.findByAppointment_AppointmentId(appt.getAppointmentId());
        assertEquals(1, byAppointment.size());

        List<Prescription> byDate = prescriptionRepository.findByDate(today);
        assertEquals(1, byDate.size());
    }

    @Test
    void testDateRangeQueries() {
        Physician phy = physicianRepository.save(createPhysician(502, "Dr Range"));
        Medication med = medicationRepository.save(createMedication(302, "MedRange"));
        Patient pat = patientRepository.save(createPatient(402, phy));

        LocalDate d1 = LocalDate.of(2024, 1, 1);
        LocalDate d2 = LocalDate.of(2024, 1, 15);
        LocalDate d3 = LocalDate.of(2024, 2, 1);

        Medication med2 = new Medication(med.getCode()+1, "M2", "B", "D");
        Medication med3 = new Medication(med.getCode()+2, "M3", "B", "D");
        medicationRepository.save(med2);
        medicationRepository.save(med3);

        Prescription p1 = new Prescription(new PrescriptionId(phy.getEmployeeId(), pat.getSsn(), med.getCode()), phy, pat, med, d1, null, "A");
        Prescription p2 = new Prescription(new PrescriptionId(phy.getEmployeeId(), pat.getSsn(), med2.getCode()), phy, pat, med2, d2, null, "B");
        Prescription p3 = new Prescription(new PrescriptionId(phy.getEmployeeId(), pat.getSsn(), med3.getCode()), phy, pat, med3, d3, null, "C");

        prescriptionRepository.save(p1);
        prescriptionRepository.save(p2);
        prescriptionRepository.save(p3);

        List<Prescription> between = prescriptionRepository.findByDateBetween(d1, d2);
        assertEquals(2, between.size());

        List<Prescription> after = prescriptionRepository.findByDateAfter(d2);
        assertEquals(1, after.size());
    }
}
