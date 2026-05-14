package com.hospital.repository;

import com.hospital.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Optional<Department> findByName(String name);

    List<Department> findByNameContainingIgnoreCase(String keyword);

    boolean existsByName(String name);
    Optional<Department> findByHead_EmployeeId(Integer physicianId);


}
