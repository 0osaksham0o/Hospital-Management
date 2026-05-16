package com.hospital.service.impl;

import com.hospital.entity.Department;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.DepartmentRepository;
import com.hospital.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository repo;

    @Override public List<Department> getAll()                           { return repo.findAll(); }
    @Override public Page<Department> getAll(Pageable p)                 { return repo.findAll(p); }
    @Override public Department getById(Integer id)                      { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found: " + id)); }
    @Override public boolean existsById(Integer id)                      { return repo.existsById(id); }
    @Override public Department save(Department d)                       { return repo.save(d); }
    @Override public void delete(Integer id)                             { getById(id); repo.deleteById(id); }

    @Override public Optional<Department> getByName(String name)         { return repo.findByName(name); }
    @Override public List<Department> searchByName(String kw)            { return repo.findByNameContainingIgnoreCase(kw); }
    @Override public boolean existsByName(String name)                   { return repo.existsByName(name); }

    @Override public Optional<Department> getByHead(Integer physicianId) { return repo.findByHead_EmployeeId(physicianId); }
    @Override public boolean existsByHead(Integer physicianId)           { return repo.existsByHead_EmployeeId(physicianId); }

    @Override public List<Department> getAllOrderedByName()               { return repo.findAllByOrderByNameAsc(); }
    @Override public List<Department> getByHeadName(String headName)     { return repo.findByHead_Name(headName); }
}

