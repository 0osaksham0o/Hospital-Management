package com.hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a physician's training/certification for a procedure.
 * Maps to the 'trained_in' table.
 */
@Entity
@Table(name = "Trained_In")
public class TrainedIn {

    @EmbeddedId
    private TrainedInId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("physicianId")
    @JoinColumn(name = "Physician", referencedColumnName = "EmployeeID")
    private Physician physician;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("treatmentCode")
    @JoinColumn(name = "Treatment", referencedColumnName = "Code")
    private Procedure treatment;

    @Column(name = "CertificationDate", nullable = false)
    private LocalDateTime certificationDate;

    @Column(name = "CertificationExpires", nullable = false)
    private LocalDateTime certificationExpires;

    public TrainedIn() {}

    public TrainedIn(TrainedInId id, Physician physician, Procedure treatment,
                     LocalDateTime certificationDate, LocalDateTime certificationExpires) {
        this.id = id;
        this.physician = physician;
        this.treatment = treatment;
        this.certificationDate = certificationDate;
        this.certificationExpires = certificationExpires;
    }

    public TrainedInId getId() { return id; }
    public void setId(TrainedInId id) { this.id = id; }

    public Physician getPhysician() { return physician; }
    public void setPhysician(Physician physician) { this.physician = physician; }

    public Procedure getTreatment() { return treatment; }
    public void setTreatment(Procedure treatment) { this.treatment = treatment; }

    public LocalDateTime getCertificationDate() { return certificationDate; }
    public void setCertificationDate(LocalDateTime certificationDate) { this.certificationDate = certificationDate; }

    public LocalDateTime getCertificationExpires() { return certificationExpires; }
    public void setCertificationExpires(LocalDateTime certificationExpires) { this.certificationExpires = certificationExpires; }

    @Override
    public String toString() {
        return "TrainedIn{id=" + id + "}";
    }
}
