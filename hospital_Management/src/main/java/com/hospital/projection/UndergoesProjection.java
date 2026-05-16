package com.hospital.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

/** Spring Data projection for Undergoes — flattens the embedded composite key and FK associations. */
public interface UndergoesProjection {
    @Value("#{target.id.patientSsn}")
    Integer getPatientSsn();

    @Value("#{target.id.procedureCode}")
    Integer getProcedureCode();

    @Value("#{target.id.stayId}")
    Integer getStayId();

    LocalDateTime getDateUndergoes();

    @Value("#{target.physician != null ? target.physician.employeeId : null}")
    Integer getPhysicianId();

    @Value("#{target.assistingNurse != null ? target.assistingNurse.employeeId : null}")
    Integer getAssistingNurseId();
}
