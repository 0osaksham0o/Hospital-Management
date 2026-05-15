package com.hospital.repository;

import com.hospital.entity.OnCall;
import com.hospital.entity.OnCallId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;


@RepositoryRestResource(path = "oncall")
public interface OnCallRepository extends JpaRepository<OnCall, OnCallId> {

    // By Nurse
    List<OnCall> findById_NurseId(Integer nurseId);
    long countById_NurseId(Integer nurseId);

    // By Block
    List<OnCall> findById_BlockFloorAndId_BlockCode(Integer blockFloor, Integer blockCode);
    List<OnCall> findById_BlockFloor(Integer blockFloor);
    List<OnCall> findById_BlockCode(Integer blockCode);

    // By Time Window
    List<OnCall> findByOnCallStartBetween(LocalDateTime from, LocalDateTime to);

    // Combined
    List<OnCall> findById_NurseIdAndId_BlockFloorAndId_BlockCode(Integer nurseId, Integer blockFloor, Integer blockCode);

}
