package com.hospital.service.impl;

import com.hospital.entity.TrainedIn;
import com.hospital.entity.TrainedInId;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.TrainedInRepository;
import com.hospital.service.TrainedInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrainedInServiceImpl implements TrainedInService {

    @Autowired
    private TrainedInRepository repo;

    @Override public List<TrainedIn> getAll()                                              { return repo.findAll(); }
    @Override public Page<TrainedIn> getAll(Pageable p)                                    { return repo.findAll(p); }
    @Override public TrainedIn getById(TrainedInId id)                                     { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("TrainedIn record not found")); }
    @Override public boolean existsById(TrainedInId id)                                    { return repo.existsById(id); }
    @Override public TrainedIn save(TrainedIn t)                                           { return repo.save(t); }
    @Override public void delete(TrainedInId id)                                           { getById(id); repo.deleteById(id); }

    @Override public List<TrainedIn> getByPhysician(Integer physicianId)                   { return repo.findById_PhysicianId(physicianId); }
    @Override public long countByPhysician(Integer physicianId)                            { return repo.countById_PhysicianId(physicianId); }

    @Override public List<TrainedIn> getByProcedure(Integer treatmentCode)                 { return repo.findById_TreatmentCode(treatmentCode); }
    @Override public long countByProcedure(Integer treatmentCode)                          { return repo.countById_TreatmentCode(treatmentCode); }

    @Override public List<TrainedIn> getByCertificationDateBetween(LocalDateTime f, LocalDateTime t) { return repo.findByCertificationDateBetween(f, t); }

    @Override public List<TrainedIn> getExpiredBefore(LocalDateTime dateTime)              { return repo.findByCertificationExpiresBefore(dateTime); }
    @Override public List<TrainedIn> getValidCertifications(LocalDateTime now)             { return repo.findByCertificationExpiresAfterOrderByCertificationExpiresAsc(now); }
    @Override public List<TrainedIn> getExpiringSoon(LocalDateTime now, LocalDateTime deadline) { return repo.findByCertificationExpiresBetweenOrderByCertificationExpiresAsc(now, deadline); }
    @Override public List<TrainedIn> getExpiredByPhysician(Integer physicianId, LocalDateTime now) { return repo.findById_PhysicianIdAndCertificationExpiresBefore(physicianId, now); }

    @Override public boolean isCertified(Integer physicianId, Integer treatmentCode)       { return repo.existsById_PhysicianIdAndId_TreatmentCode(physicianId, treatmentCode); }
}
