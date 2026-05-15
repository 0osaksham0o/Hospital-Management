package com.hospital.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class TrainedInId implements Serializable {

    @Column(name = "Physician")
    private Integer physicianId;

    @Column(name = "Treatment")
    private Integer treatmentCode;

    public TrainedInId() {}

    public TrainedInId(Integer physicianId, Integer treatmentCode) {
        this.physicianId = physicianId;
        this.treatmentCode = treatmentCode;
    }

    public Integer getPhysicianId() { return physicianId; }
    public void setPhysicianId(Integer physicianId) { this.physicianId = physicianId; }

    public Integer getTreatmentCode() { return treatmentCode; }
    public void setTreatmentCode(Integer treatmentCode) { this.treatmentCode = treatmentCode; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrainedInId)) return false;
        TrainedInId that = (TrainedInId) o;
        return Objects.equals(physicianId, that.physicianId) && Objects.equals(treatmentCode, that.treatmentCode);
    }

    @Override
    public int hashCode() { return Objects.hash(physicianId, treatmentCode); }
}
