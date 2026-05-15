package com.hospital.repository;

import com.hospital.entity.Stay;
import com.hospital.projection.StayProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RepositoryRestResource(path = "stays")
public interface StayRepository extends JpaRepository<Stay, Integer> {


    Page<StayProjection> findAllProjectedBy(Pageable pageable);
    Optional<StayProjection> findProjectedByStayId(Integer stayId);


    List<Stay> findByPatient_Ssn(Integer patientSsn);
    long countByPatient_Ssn(Integer patientSsn);


    List<Stay> findByRoom_RoomNumber(Integer roomNumber);
    long countByRoom_RoomNumber(Integer roomNumber);


    List<Stay> findByStayStartBetween(LocalDateTime from, LocalDateTime to);
    List<Stay> findByStayEndBetween(LocalDateTime from, LocalDateTime to);


    List<Stay> findByPatient_SsnAndRoom_RoomNumber(Integer patientSsn, Integer roomNumber);

    List<Stay> findByStayEndAfterOrderByStayStartAsc(LocalDateTime now);

    List<Stay> findAllByOrderByStayStartDesc();
}
