package com.hospital.projection;

import org.springframework.beans.factory.annotation.Value;

public interface PatientProjection {
    Integer getSsn();
    String getName();
    String getAddress();
    String getPhone();
    Integer getInsuranceId();

    @Value("#{target.primaryCarePhysician != null ? target.primaryCarePhysician.employeeId : null}")
    Integer getPrimaryCarePhysicianId();

    @Value("#{target.primaryCarePhysician != null ? target.primaryCarePhysician.name : null}")
    String getPrimaryCarePhysicianName();
}