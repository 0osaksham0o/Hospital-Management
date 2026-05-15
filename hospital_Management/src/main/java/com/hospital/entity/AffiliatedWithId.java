package com.hospital.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AffiliatedWithId implements Serializable {

    @Column(name = "Physician")
    private Integer physicianId;

    @Column(name = "Department")
    private Integer departmentId;

    public AffiliatedWithId() {}

    public AffiliatedWithId(Integer physicianId, Integer departmentId) {
        this.physicianId = physicianId;
        this.departmentId = departmentId;
    }

    public Integer getPhysicianId() { return physicianId; }
    public void setPhysicianId(Integer physicianId) { this.physicianId = physicianId; }

    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AffiliatedWithId)) return false;
        AffiliatedWithId that = (AffiliatedWithId) o;
        return Objects.equals(physicianId, that.physicianId) && Objects.equals(departmentId, that.departmentId);
    }

    @Override
    public int hashCode() { return Objects.hash(physicianId, departmentId); }
}
