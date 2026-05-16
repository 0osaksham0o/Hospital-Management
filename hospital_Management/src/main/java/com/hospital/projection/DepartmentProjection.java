package com.hospital.projection;
import org.springframework.beans.factory.annotation.Value;

/** Spring Data projection for Department — exposes flat scalar fields only. */
public interface DepartmentProjection {
    Integer getDepartmentId();
    String getName();

    /** Navigates into the head association to pull only the employeeId. */
    @Value("#{target.head != null ? target.head.employeeId : null}")
    Integer getHeadId();

    /** Head physician's display name for UI convenience. */
    @Value("#{target.head != null ? target.head.name : null}")
    String getHeadName();
}