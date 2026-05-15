package com.hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "Appointment")
public class Appointment {

    @Id
    @Column(name = "AppointmentID", nullable = false, unique = true)
    private Integer appointmentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Patient", referencedColumnName = "SSN", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PrepNurse", referencedColumnName = "EmployeeID", nullable = true)
    private Nurse prepNurse;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Physician", referencedColumnName = "EmployeeID", nullable = false)
    private Physician physician;

    @Column(name = "Starto", nullable = false)
    private LocalDateTime start;

    @Column(name = "Endo", nullable = false)
    private LocalDateTime end;

    @Column(name = "ExaminationRoom", nullable = false, columnDefinition = "TEXT")
    private String examinationRoom;

    public Appointment() {}

    public Appointment(Integer appointmentId, Patient patient, Nurse prepNurse, Physician physician,
                       LocalDateTime start, LocalDateTime end, String examinationRoom) {
        this.appointmentId   = appointmentId;
        this.patient         = patient;
        this.prepNurse       = prepNurse;
        this.physician       = physician;
        this.start           = start;
        this.end             = end;
        this.examinationRoom = examinationRoom;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────

    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Nurse getPrepNurse() { return prepNurse; }
    public void setPrepNurse(Nurse prepNurse) { this.prepNurse = prepNurse; }

    public Physician getPhysician() { return physician; }
    public void setPhysician(Physician physician) { this.physician = physician; }

    public LocalDateTime getStart() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; }

    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }

    public String getExaminationRoom() { return examinationRoom; }
    public void setExaminationRoom(String examinationRoom) { this.examinationRoom = examinationRoom; }

    @Override
    public String toString() {
        return "Appointment{appointmentId=" + appointmentId + "}";
    }
}
