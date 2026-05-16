package com.hospital.projection;

/** Spring Data projection for Nurse — exposes flat scalar fields only. */
public interface NurseProjection {
    Integer getEmployeeId();
    String getName();
    String getPosition();
    Boolean getRegistered();
    Integer getSsn();
}
