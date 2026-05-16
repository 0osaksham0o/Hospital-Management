package com.hospital.service.impl;

import com.hospital.entity.OnCall;
import com.hospital.entity.OnCallId;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.OnCallRepository;
import com.hospital.service.OnCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OnCallServiceImpl implements OnCallService {

    @Autowired
    private OnCallRepository repo;

    @Override public List<OnCall> getAll()                                                     { return repo.findAll(); }
    @Override public Page<OnCall> getAll(Pageable p)                                           { return repo.findAll(p); }
    @Override public OnCall getById(OnCallId id)                                               { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("OnCall record not found")); }
    @Override public boolean existsById(OnCallId id)                                           { return repo.existsById(id); }
    @Override public OnCall save(OnCall o)                                                     { return repo.save(o); }
    @Override public void delete(OnCallId id)                                                  { getById(id); repo.deleteById(id); }

    @Override public List<OnCall> getByNurse(Integer nurseId)                                  { return repo.findById_NurseId(nurseId); }
    @Override public long countByNurse(Integer nurseId)                                        { return repo.countById_NurseId(nurseId); }

    @Override public List<OnCall> getByBlock(Integer floor, Integer code)                      { return repo.findById_BlockFloorAndId_BlockCode(floor, code); }
    @Override public List<OnCall> getByFloor(Integer floor)                                    { return repo.findById_BlockFloor(floor); }
    @Override public List<OnCall> getByBlockCode(Integer code)                                 { return repo.findById_BlockCode(code); }

    @Override public List<OnCall> getByStartBetween(LocalDateTime from, LocalDateTime to)      { return repo.findByOnCallStartBetween(from, to); }
    @Override public List<OnCall> getCurrentlyOnCall(LocalDateTime now)                        { return repo.findByOnCallStartLessThanEqualAndOnCallEndGreaterThanEqual(now, now); }
    @Override public List<OnCall> getNurseShiftsInWindow(Integer nurseId, LocalDateTime f, LocalDateTime t) { return repo.findById_NurseIdAndOnCallStartBeforeAndOnCallEndAfter(nurseId, t, f); }

    @Override public List<OnCall> getByNurseAndBlock(Integer nurseId, Integer floor, Integer code) { return repo.findById_NurseIdAndId_BlockFloorAndId_BlockCode(nurseId, floor, code); }

    @Override public List<OnCall> getAllOrderedByStart()                                       { return repo.findAllByOrderByOnCallStartAsc(); }
}
