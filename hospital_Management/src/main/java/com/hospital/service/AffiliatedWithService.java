package com.hospital.service;


import com.hospital.entity.AffiliatedWith;
import com.hospital.entity.AffiliatedWithId;
import com.hospital.entity.Department;
import com.hospital.entity.Physician;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AffiliatedWithService {
    List<AffiliatedWith> getAll();
    Page<AffiliatedWith> getAll(Pageable pageable);
    AffiliatedWith getById(AffiliatedWithId id);
    boolean existsById(AffiliatedWithId id);
    AffiliatedWith save(AffiliatedWith affiliatedWith);
    void delete(AffiliatedWithId id);

    // By Physician
    List<AffiliatedWith> getByPhysician(Integer physicianId);
    long countByPhysician(Integer physicianId);

    // By Department
    List<AffiliatedWith> getByDepartment(Integer departmentId);
    long countByDepartment(Integer departmentId);

    // By Primary Affiliation
    List<AffiliatedWith> getByDepartmentAndPrimary(Integer departmentId, Boolean primary);
    List<AffiliatedWith> getByPhysicianAndPrimary(Integer physicianId, Boolean primary);
    List<AffiliatedWith> getAllByPrimary(Boolean primary);

    // Existence
    boolean isAffiliated(Integer physicianId, Integer departmentId);

    // Complex Queries
    List<Physician> getPhysiciansWithPrimaryAffiliation();
    List<Department> getDepartmentsWithPrimaryAffiliations();
}

