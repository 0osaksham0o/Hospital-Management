package com.hospital.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Patient")
public class Patient {

    @Id
    @Column(name = "SSN", nullable = false, unique = true)
    private Integer ssn;

    @Column(name = "Name", nullable = false, length = 30)
    private String name;

    @Column(name = "Address", nullable = false, length = 30)
    private String address;

    @Column(name = "Phone", nullable = false, length = 30)
    private String phone;

    @Column(name = "InsuranceID", nullable = false)
    private Integer insuranceId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PCP", referencedColumnName = "EmployeeID", nullable = false)
    private Physician primaryCarePhysician;

    public Patient() {}

    public Patient(Integer ssn, String name, String address, String phone, Integer insuranceId, Physician primaryCarePhysician) {
        this.ssn = ssn;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.insuranceId = insuranceId;
        this.primaryCarePhysician = primaryCarePhysician;
    }

    public Integer getSsn() { return ssn; }
    public void setSsn(Integer ssn) { this.ssn = ssn; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Integer getInsuranceId() { return insuranceId; }
    public void setInsuranceId(Integer insuranceId) { this.insuranceId = insuranceId; }

    public Physician getPrimaryCarePhysician() { return primaryCarePhysician; }
    public void setPrimaryCarePhysician(Physician primaryCarePhysician) { this.primaryCarePhysician = primaryCarePhysician; }

    @Override
    public String toString() {
        return "Patient{ssn=" + ssn + ", name=" + name + "}";
    }
}
