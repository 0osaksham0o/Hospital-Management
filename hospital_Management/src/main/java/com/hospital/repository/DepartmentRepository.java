package com.hospital.repository;

import com.hospital.entity.Department;
import com.hospital.projection.DepartmentProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;


@RepositoryRestResource(exported = false)
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Page<DepartmentProjection> findAllProjectedBy(Pageable pageable);
    Optional<DepartmentProjection> findProjectedByDepartmentId(Integer departmentId);



    Optional<Department> findByName(String name);


    List<Department> findByNameContainingIgnoreCase(String keyword);

    boolean existsByName(String name);


    Optional<Department> findByHead_EmployeeId(Integer physicianId);


    boolean existsByHead_EmployeeId(Integer physicianId);


    List<Department> findAllByOrderByNameAsc();

    List<Department> findByHead_Name(String headName);
}
