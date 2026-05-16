package com.hospital.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

/** Spring Data projection for Appointment — flattens FK associations to scalar IDs. */
public interface AppointmentProjection {
    Integer getAppointmentId();

    @Value("#{target.patient != null ? target.patient.ssn : null}")
    Integer getPatientSsn();

    @Value("#{target.patient != null ? target.patient.name : null}")
    String getPatientName();

    @Value("#{target.prepNurse != null ? target.prepNurse.employeeId : null}")
    Integer getPrepNurseId();

    @Value("#{target.prepNurse != null ? target.prepNurse.name : null}")
    String getPrepNurseName();

    @Value("#{target.physician != null ? target.physician.employeeId : null}")
    Integer getPhysicianId();

    @Value("#{target.physician != null ? target.physician.name : null}")
    String getPhysicianName();

    LocalDateTime getStart();
    LocalDateTime getEnd();
    String getExaminationRoom();
}
