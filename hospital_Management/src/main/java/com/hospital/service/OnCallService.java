package com.hospital.service;

import com.hospital.entity.OnCall;
import com.hospital.entity.OnCallId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OnCallService {
    List<OnCall> getAll();
    Page<OnCall> getAll(Pageable pageable);
    OnCall getById(OnCallId id);
    boolean existsById(OnCallId id);
    OnCall save(OnCall onCall);
    void delete(OnCallId id);

    // By Nurse
    List<OnCall> getByNurse(Integer nurseId);
    long countByNurse(Integer nurseId);

    // By Block
    List<OnCall> getByBlock(Integer blockFloor, Integer blockCode);
    List<OnCall> getByFloor(Integer blockFloor);
    List<OnCall> getByBlockCode(Integer blockCode);

    // By Time
    List<OnCall> getByStartBetween(LocalDateTime from, LocalDateTime to);
    List<OnCall> getCurrentlyOnCall(LocalDateTime now);
    List<OnCall> getNurseShiftsInWindow(Integer nurseId, LocalDateTime from, LocalDateTime to);

    // Combined
    List<OnCall> getByNurseAndBlock(Integer nurseId, Integer blockFloor, Integer blockCode);

    // Sorted
    List<OnCall> getAllOrderedByStart();
}
