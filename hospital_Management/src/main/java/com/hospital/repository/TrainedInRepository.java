package com.hospital.repository;

import com.hospital.entity.TrainedIn;
import com.hospital.entity.TrainedInId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;


@RepositoryRestResource(path = "trainedin")
public interface TrainedInRepository extends JpaRepository<TrainedIn, TrainedInId> {


    List<TrainedIn> findById_PhysicianId(Integer physicianId);
    long countById_PhysicianId(Integer physicianId);


    List<TrainedIn> findById_TreatmentCode(Integer treatmentCode);
    long countById_TreatmentCode(Integer treatmentCode);


    List<TrainedIn> findByCertificationDateBetween(LocalDateTime from, LocalDateTime to);


    List<TrainedIn> findByCertificationExpiresBefore(LocalDateTime dateTime);


    boolean existsById_PhysicianIdAndId_TreatmentCode(Integer physicianId, Integer treatmentCode);


}

