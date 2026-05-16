package com.hospital.projection;

import org.springframework.beans.factory.annotation.Value;

/** Spring Data projection for AffiliatedWith — flattens the embedded composite key. */
public interface AffiliatedWithProjection {
    @Value("#{target.id.physicianId}")
    Integer getPhysicianId();

    @Value("#{target.id.departmentId}")
    Integer getDepartmentId();

    Boolean getPrimaryAffiliation();
}