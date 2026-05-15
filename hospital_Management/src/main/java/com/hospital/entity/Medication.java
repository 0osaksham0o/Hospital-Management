package com.hospital.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Medication")
public class Medication {

    @Id
    @Column(name = "Code", nullable = false, unique = true)
    private Integer code;

    @Column(name = "Name", nullable = false, length = 30)
    private String name;

    @Column(name = "Brand", nullable = false, length = 30)
    private String brand;

    @Column(name = "Description", nullable = false, length = 30)
    private String description;

    public Medication() {}

    public Medication(Integer code, String name, String brand, String description) {
        this.code = code;
        this.name = name;
        this.brand = brand;
        this.description = description;
    }

    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Medication{code=" + code + ", name=" + name + ", brand=" + brand + "}";
    }
}
