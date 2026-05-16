package com.hospital.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface TrainedInProjection {
    @Value("#{target.id.physicianId}")
    Integer getPhysicianId();

    @Value("#{target.id.treatmentCode}")
    Integer getTreatmentCode();

    LocalDateTime getCertificationDate();
    LocalDateTime getCertificationExpires();
}

