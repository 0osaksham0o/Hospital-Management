package com.hospital.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class UndergoesId implements Serializable {

    @Column(name = "Patient")
    private Integer patientSsn;

    @Column(name = "Procedure")
    private Integer procedureCode;

    @Column(name = "Stay")
    private Integer stayId;

    public UndergoesId() {}

    public UndergoesId(Integer patientSsn, Integer procedureCode, Integer stayId) {
        this.patientSsn = patientSsn;
        this.procedureCode = procedureCode;
        this.stayId = stayId;
    }

    public Integer getPatientSsn() { return patientSsn; }
    public void setPatientSsn(Integer patientSsn) { this.patientSsn = patientSsn; }

    public Integer getProcedureCode() { return procedureCode; }
    public void setProcedureCode(Integer procedureCode) { this.procedureCode = procedureCode; }

    public Integer getStayId() { return stayId; }
    public void setStayId(Integer stayId) { this.stayId = stayId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UndergoesId that)) return false;
        return Objects.equals(patientSsn, that.patientSsn) &&
                Objects.equals(procedureCode, that.procedureCode) &&
                Objects.equals(stayId, that.stayId);
    }

    @Override
    public int hashCode() { return Objects.hash(patientSsn, procedureCode, stayId); }
}
