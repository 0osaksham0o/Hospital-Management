package com.hospital.service.impl;


import com.hospital.entity.AffiliatedWith;
import com.hospital.entity.AffiliatedWithId;
import com.hospital.entity.Department;
import com.hospital.entity.Physician;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.AffiliatedWithRepository;
import com.hospital.service.AffiliatedWithService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AffiliatedWithServiceImpl implements AffiliatedWithService {

    @Autowired
    private AffiliatedWithRepository repo;

    @Override public List<AffiliatedWith> getAll()                                          { return repo.findAll(); }
    @Override public Page<AffiliatedWith> getAll(Pageable p)                                { return repo.findAll(p); }
    @Override public AffiliatedWith getById(AffiliatedWithId id)                            { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Affiliation not found")); }
    @Override public boolean existsById(AffiliatedWithId id)                                { return repo.existsById(id); }
    @Override public AffiliatedWith save(AffiliatedWith a)                                  { return repo.save(a); }
    @Override public void delete(AffiliatedWithId id)                                       { getById(id); repo.deleteById(id); }

    @Override public List<AffiliatedWith> getByPhysician(Integer physicianId)               { return repo.findById_PhysicianId(physicianId); }
    @Override public long countByPhysician(Integer physicianId)                             { return repo.countById_PhysicianId(physicianId); }

    @Override public List<AffiliatedWith> getByDepartment(Integer departmentId)             { return repo.findById_DepartmentId(departmentId); }
    @Override public long countByDepartment(Integer departmentId)                           { return repo.countById_DepartmentId(departmentId); }

    @Override public List<AffiliatedWith> getByDepartmentAndPrimary(Integer deptId, Boolean primary) { return repo.findById_DepartmentIdAndPrimaryAffiliation(deptId, primary); }
    @Override public List<AffiliatedWith> getByPhysicianAndPrimary(Integer physId, Boolean primary)  { return repo.findById_PhysicianIdAndPrimaryAffiliation(physId, primary); }
    @Override public List<AffiliatedWith> getAllByPrimary(Boolean primary)                   { return repo.findByPrimaryAffiliation(primary); }

    @Override public boolean isAffiliated(Integer physicianId, Integer departmentId)        { return repo.existsById_PhysicianIdAndId_DepartmentId(physicianId, departmentId); }

    @Override public List<Physician> getPhysiciansWithPrimaryAffiliation() {
        return repo.findByPrimaryAffiliation(true).stream()
                .map(AffiliatedWith::getPhysician)
                .distinct()
                .collect(Collectors.toList());
    }
    @Override public List<Department> getDepartmentsWithPrimaryAffiliations() {
        return repo.findByPrimaryAffiliation(true).stream()
                .map(AffiliatedWith::getDepartment)
                .distinct()
                .collect(Collectors.toList());
    }
}

