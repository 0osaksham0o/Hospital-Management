package com.hospital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "Affiliated_With")
public class AffiliatedWith {

    @EmbeddedId
    private AffiliatedWithId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("physicianId")
    @JoinColumn(name = "Physician", referencedColumnName = "EmployeeID")
    private Physician physician;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("departmentId")
    @JoinColumn(name = "Department", referencedColumnName = "DepartmentID")
    private Department department;

    @Column(name = "PrimaryAffiliation", nullable = false)
    private Boolean primaryAffiliation;

    @Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AffiliatedWith)) return false;

    AffiliatedWith that = (AffiliatedWith) o;

    return id != null && id.equals(that.id);
}

@Override
public int hashCode() {
    return id != null ? id.hashCode() : 0;
}

    public AffiliatedWith() {}

    public AffiliatedWith(AffiliatedWithId id, Physician physician, Department department, Boolean primaryAffiliation) {
        this.id = id;
        this.physician = physician;
        this.department = department;
        this.primaryAffiliation = primaryAffiliation;
    }

    public AffiliatedWithId getId() { return id; }
    public void setId(AffiliatedWithId id) { this.id = id; }

    public Physician getPhysician() { return physician; }
    public void setPhysician(Physician physician) { this.physician = physician; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public Boolean getPrimaryAffiliation() { return primaryAffiliation; }
    public void setPrimaryAffiliation(Boolean primaryAffiliation) { this.primaryAffiliation = primaryAffiliation; }

    @Override
    public String toString() {
        return "AffiliatedWith{id=" + id + ", primaryAffiliation=" + primaryAffiliation + "}";
    }
}


