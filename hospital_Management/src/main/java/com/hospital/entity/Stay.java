package com.hospital.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "Stay")
public class Stay {

    @Id
    @Column(name = "StayID", nullable = false, unique = true)
    private Integer stayId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Patient", referencedColumnName = "SSN", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Room", referencedColumnName = "RoomNumber", nullable = false)
    private Room room;

    @Column(name = "StayStart", nullable = false)
    private LocalDateTime stayStart;

    @Column(name = "StayEnd", nullable = false)
    private LocalDateTime stayEnd;

    public Stay() {}

    public Stay(Integer stayId, Patient patient, Room room, LocalDateTime stayStart, LocalDateTime stayEnd) {
        this.stayId = stayId;
        this.patient = patient;
        this.room = room;
        this.stayStart = stayStart;
        this.stayEnd = stayEnd;
    }

    public Integer getStayId() { return stayId; }
    public void setStayId(Integer stayId) { this.stayId = stayId; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }

    public LocalDateTime getStayStart() { return stayStart; }
    public void setStayStart(LocalDateTime stayStart) { this.stayStart = stayStart; }

    public LocalDateTime getStayEnd() { return stayEnd; }
    public void setStayEnd(LocalDateTime stayEnd) { this.stayEnd = stayEnd; }

    @Override
    public String toString() {
        return "Stay{stayId=" + stayId + "}";
    }
}
