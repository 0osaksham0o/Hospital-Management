package com.hospital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Procedures")
public class Procedure {

    @Id
    @Column(name = "Code", nullable = false, unique = true)
    private Integer code;

    @Column(name = "Name", nullable = false, length = 32)
    private String name;

    @Column(name = "Cost", nullable = false)
    private Double cost;

    public Procedure() {}

    public Procedure(Integer code, String name, Double cost) {
        this.code = code;
        this.name = name;
        this.cost = cost;
    }

    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    @Override
    public String toString() {
        return "Procedure{code=" + code + ", name=" + name + ", cost=" + cost + "}";
    }
}
