package com.hospital.repository;

import com.hospital.entity.Undergoes;
import com.hospital.entity.UndergoesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource(path = "undergoes")
public interface UndergoesRepository extends JpaRepository<Undergoes, UndergoesId> {

    List<Undergoes> findById_PatientSsn(Integer patientSsn);

    long countById_PatientSsn(Integer patientSsn);

    List<Undergoes> findById_ProcedureCode(Integer procedureCode);

    List<Undergoes> findById_StayId(Integer stayId);

    List<Undergoes> findByPhysician_EmployeeId(Integer physicianId);

    long countByPhysician_EmployeeId(Integer physicianId);

    List<Undergoes> findByAssistingNurse_EmployeeId(Integer nurseId);

    List<Undergoes> findByDateUndergoesBetween(LocalDateTime from, LocalDateTime to);

    List<Undergoes> findByDateUndergoesAfter(LocalDateTime dateTime);

    List<Undergoes> findById_PatientSsnAndPhysician_EmployeeId(Integer patientSsn, Integer physicianId);

}
