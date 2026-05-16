package com.hospital.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public interface PrescriptionProjection {
    @Value("#{target.id.physicianId}")
    Integer getPhysicianId();

    @Value("#{target.id.patientSsn}")
    Integer getPatientSsn();

    @Value("#{target.id.medicationCode}")
    Integer getMedicationCode();

    LocalDate getDate();
    String getDose();

    @Value("#{target.appointment != null ? target.appointment.appointmentId : null}")
    Integer getAppointmentId();
}
