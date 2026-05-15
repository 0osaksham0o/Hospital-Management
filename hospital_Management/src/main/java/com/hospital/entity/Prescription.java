package com.hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Prescribes")
public class Prescription {

    @EmbeddedId
    private PrescriptionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("physicianId")
    @JoinColumn(name = "Physician", referencedColumnName = "EmployeeID")
    private Physician physician;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("patientSsn")
    @JoinColumn(name = "Patient", referencedColumnName = "SSN")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("medicationCode")
    @JoinColumn(name = "Medication", referencedColumnName = "Code")
    private Medication medication;

    @Column(name = "date", nullable = true)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Appointment", referencedColumnName = "AppointmentID", nullable = true)
    private Appointment appointment;

    @Column(name = "Dose", length = 30)
    private String dose;

    public Prescription() {}

    public Prescription(PrescriptionId id, Physician physician, Patient patient, Medication medication,
                        LocalDate date, Appointment appointment, String dose) {
        this.id = id;
        this.physician = physician;
        this.patient = patient;
        this.medication = medication;
        this.date = date;
        this.appointment = appointment;
        this.dose = dose;
    }

    public PrescriptionId getId() { return id; }
    public void setId(PrescriptionId id) { this.id = id; }

    public Physician getPhysician() { return physician; }
    public void setPhysician(Physician physician) { this.physician = physician; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Medication getMedication() { return medication; }
    public void setMedication(Medication medication) { this.medication = medication; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }

    public String getDose() { return dose; }
    public void setDose(String dose) { this.dose = dose; }

    @Override
    public String toString() {
        return "Prescription{id=" + id + "}";
    }
}
