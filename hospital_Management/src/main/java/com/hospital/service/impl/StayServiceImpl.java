package com.hospital.service.impl;

import com.hospital.entity.Stay;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.StayRepository;
import com.hospital.service.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StayServiceImpl implements StayService {

    @Autowired
    private StayRepository repo;

    @Override public List<Stay> getAll()                                              { return repo.findAll(); }
    @Override public Page<Stay> getAll(Pageable p)                                    { return repo.findAll(p); }
    @Override public Stay getById(Integer stayId)                                     { return repo.findById(stayId).orElseThrow(() -> new ResourceNotFoundException("Stay not found: " + stayId)); }
    @Override public boolean existsById(Integer stayId)                               { return repo.existsById(stayId); }
    @Override public Stay save(Stay s)                                                { return repo.save(s); }
    @Override public void delete(Integer stayId)                                      { getById(stayId); repo.deleteById(stayId); }

    @Override public List<Stay> getByPatient(Integer ssn)                             { return repo.findByPatient_Ssn(ssn); }
    @Override public long countByPatient(Integer ssn)                                 { return repo.countByPatient_Ssn(ssn); }

    @Override public List<Stay> getByRoom(Integer roomNumber)                         { return repo.findByRoom_RoomNumber(roomNumber); }
    @Override public long countByRoom(Integer roomNumber)                             { return repo.countByRoom_RoomNumber(roomNumber); }

    @Override public List<Stay> getByStartBetween(LocalDateTime from, LocalDateTime to) { return repo.findByStayStartBetween(from, to); }
    @Override public List<Stay> getByEndBetween(LocalDateTime from, LocalDateTime to)   { return repo.findByStayEndBetween(from, to); }
    @Override public List<Stay> getCurrentStays(LocalDateTime now)                     { return repo.findByStayEndAfterOrderByStayStartAsc(now); }

    @Override public List<Stay> getByPatientAndRoom(Integer ssn, Integer roomNumber)  { return repo.findByPatient_SsnAndRoom_RoomNumber(ssn, roomNumber); }

    @Override public List<Stay> getAllOrderedByStartDesc()                             { return repo.findAllByOrderByStayStartDesc(); }
}
