package com.hospital.entity;

import jakarta.persistence.*;

/**
 * Entity representing a Department in the hospital.
 * Maps to the 'department' table.
 */
@Entity
@Table(name = "department")
public class Department {

    @Id
    @Column(name = "DepartmentID", nullable = false, unique = true)
    private Integer departmentId;

    @Column(name = "Name", nullable = false, length = 32)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Head", referencedColumnName = "EmployeeID", nullable = false)
    private Physician head;

    public Department() {}

    public Department(Integer departmentId, String name, Physician head) {
        this.departmentId = departmentId;
        this.name = name;
        this.head = head;
    }

    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Physician getHead() { return head; }
    public void setHead(Physician head) { this.head = head; }

    @Override
    public String toString() {
        return "Department{departmentId=" + departmentId + ", name=" + name + "}";
    }
}
