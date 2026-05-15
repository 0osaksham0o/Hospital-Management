package com.hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Undergoes")
public class Undergoes {

    @EmbeddedId
    private UndergoesId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Patient", referencedColumnName = "SSN",
            insertable = false, updatable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Procedure", referencedColumnName = "Code",
            insertable = false, updatable = false)
    private Procedure procedure;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Stay", referencedColumnName = "StayID",
            insertable = false, updatable = false)
    private Stay stay;

    @Column(name = "DateUndergoes", nullable = false)
    private LocalDateTime dateUndergoes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Physician", referencedColumnName = "EmployeeID", nullable = true)
    private Physician physician;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AssistingNurse", referencedColumnName = "EmployeeID", nullable = true)
    private Nurse assistingNurse;

    public Undergoes() {}

    public Undergoes(UndergoesId id, Patient patient, Procedure procedure, Stay stay,
                     LocalDateTime dateUndergoes, Physician physician, Nurse assistingNurse) {
        this.id = id;
        this.patient = patient;
        this.procedure = procedure;
        this.stay = stay;
        this.dateUndergoes = dateUndergoes;
        this.physician = physician;
        this.assistingNurse = assistingNurse;
    }

    public UndergoesId getId() { return id; }
    public void setId(UndergoesId id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Procedure getProcedure() { return procedure; }
    public void setProcedure(Procedure procedure) { this.procedure = procedure; }

    public Stay getStay() { return stay; }
    public void setStay(Stay stay) { this.stay = stay; }

    public LocalDateTime getDateUndergoes() { return dateUndergoes; }
    public void setDateUndergoes(LocalDateTime dateUndergoes) { this.dateUndergoes = dateUndergoes; }

    public Physician getPhysician() { return physician; }
    public void setPhysician(Physician physician) { this.physician = physician; }

    public Nurse getAssistingNurse() { return assistingNurse; }
    public void setAssistingNurse(Nurse assistingNurse) { this.assistingNurse = assistingNurse; }

    @Override
    public String toString() {
        return "Undergoes{id=" + id + "}";
    }
}
