package com.hospital.service;

import com.hospital.entity.TrainedIn;
import com.hospital.entity.TrainedInId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainedInService {
    List<TrainedIn> getAll();
    Page<TrainedIn> getAll(Pageable pageable);
    TrainedIn getById(TrainedInId id);
    boolean existsById(TrainedInId id);
    TrainedIn save(TrainedIn trainedIn);
    void delete(TrainedInId id);


    List<TrainedIn> getByPhysician(Integer physicianId);
    long countByPhysician(Integer physicianId);


    List<TrainedIn> getByProcedure(Integer treatmentCode);
    long countByProcedure(Integer treatmentCode);


    List<TrainedIn> getByCertificationDateBetween(LocalDateTime from, LocalDateTime to);


    List<TrainedIn> getExpiredBefore(LocalDateTime dateTime);
    List<TrainedIn> getValidCertifications(LocalDateTime now);
    List<TrainedIn> getExpiringSoon(LocalDateTime now, LocalDateTime deadline);
    List<TrainedIn> getExpiredByPhysician(Integer physicianId, LocalDateTime now);


    boolean isCertified(Integer physicianId, Integer treatmentCode);
}
