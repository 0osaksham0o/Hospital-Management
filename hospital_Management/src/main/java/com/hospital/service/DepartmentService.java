package com.hospital.service;


import com.hospital.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    List<Department> getAll();
    Page<Department> getAll(Pageable pageable);
    Department getById(Integer id);
    boolean existsById(Integer id);
    Department save(Department department);
    void delete(Integer id);

    // By Name
    Optional<Department> getByName(String name);
    List<Department> searchByName(String keyword);
    boolean existsByName(String name);

    // By Head
    Optional<Department> getByHead(Integer physicianId);
    boolean existsByHead(Integer physicianId);

    // Sorted / Queries
    List<Department> getAllOrderedByName();
    List<Department> getByHeadName(String headName);
}
