package com.hospital.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface StayProjection {
    Integer getStayId();

    @Value("#{target.patient != null ? target.patient.ssn : null}")
    Integer getPatientSsn();

    @Value("#{target.room != null ? target.room.roomNumber : null}")
    Integer getRoomNumber();

    LocalDateTime getStayStart();
    LocalDateTime getStayEnd();
}
