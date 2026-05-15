package com.hospital.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "Nurse")
public class Nurse {

    @Id
    @Column(name = "EmployeeID", nullable = false, unique = true)
    private Integer employeeId;

    @Column(name = "Name", nullable = false, length = 32)
    private String name;

    @Column(name = "Position", nullable = false, length = 32)
    private String position;

    @Column(name = "Registered", nullable = false)
    private Boolean registered;

    @Column(name = "SSN", nullable = false)
    private Integer ssn;

    public Nurse() {}

    public Nurse(Integer employeeId, String name, String position, Boolean registered, Integer ssn) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
        this.registered = registered;
        this.ssn = ssn;
    }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public Boolean getRegistered() { return registered; }
    public void setRegistered(Boolean registered) { this.registered = registered; }

    public Integer getSsn() { return ssn; }
    public void setSsn(Integer ssn) { this.ssn = ssn; }

    @Override
    public String toString() {
        return "Nurse{employeeId=" + employeeId + ", name=" + name + ", registered=" + registered + "}";
    }
}
