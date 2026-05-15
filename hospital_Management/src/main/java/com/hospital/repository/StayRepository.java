package com.hospital.repository;

import com.hospital.entity.Stay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;


@RepositoryRestResource(path = "stays")
public interface StayRepository extends JpaRepository<Stay, Integer> {


    List<Stay> findByPatient_Ssn(Integer patientSsn);
    long countByPatient_Ssn(Integer patientSsn);


    List<Stay> findByRoom_RoomNumber(Integer roomNumber);
    long countByRoom_RoomNumber(Integer roomNumber);


    List<Stay> findByStayStartBetween(LocalDateTime from, LocalDateTime to);
    List<Stay> findByStayEndBetween(LocalDateTime from, LocalDateTime to);


    List<Stay> findByPatient_SsnAndRoom_RoomNumber(Integer patientSsn, Integer roomNumber);


}
