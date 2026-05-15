package com.hospital.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class PrescriptionId implements Serializable {

    @Column(name = "Physician")
    private Integer physicianId;

    @Column(name = "Patient")
    private Integer patientSsn;

    @Column(name = "Medication")
    private Integer medicationCode;

    public PrescriptionId() {}

    public PrescriptionId(Integer physicianId, Integer patientSsn, Integer medicationCode) {
        this.physicianId = physicianId;
        this.patientSsn = patientSsn;
        this.medicationCode = medicationCode;
    }

    public Integer getPhysicianId() { return physicianId; }
    public void setPhysicianId(Integer physicianId) { this.physicianId = physicianId; }

    public Integer getPatientSsn() { return patientSsn; }
    public void setPatientSsn(Integer patientSsn) { this.patientSsn = patientSsn; }

    public Integer getMedicationCode() { return medicationCode; }
    public void setMedicationCode(Integer medicationCode) { this.medicationCode = medicationCode; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrescriptionId that)) return false;
        return Objects.equals(physicianId, that.physicianId) &&
                Objects.equals(patientSsn, that.patientSsn) &&
                Objects.equals(medicationCode, that.medicationCode);
    }

    @Override
    public int hashCode() { return Objects.hash(physicianId, patientSsn, medicationCode); }
}
