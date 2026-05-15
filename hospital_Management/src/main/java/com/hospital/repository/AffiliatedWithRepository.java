package com.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.hospital.entity.AffiliatedWith;
import com.hospital.entity.AffiliatedWithId;

@RepositoryRestResource(path = "affiliatedwith")
public interface AffiliatedWithRepository extends JpaRepository<AffiliatedWith, AffiliatedWithId> {

  
    List<AffiliatedWith> findById_PhysicianId(Integer physicianId);

    
    long countById_PhysicianId(Integer physicianId);

    /** Find all physicians affiliated with a department. */
    List<AffiliatedWith> findById_DepartmentId(Integer departmentId);

  
    long countById_DepartmentId(Integer departmentId);

  
    List<AffiliatedWith> findById_DepartmentIdAndPrimaryAffiliation(Integer departmentId, Boolean primary);

   
    List<AffiliatedWith> findById_PhysicianIdAndPrimaryAffiliation(Integer physicianId, Boolean primary);

    
    List<AffiliatedWith> findByPrimaryAffiliation(Boolean primary);

   
    boolean existsById_PhysicianIdAndId_DepartmentId(Integer physicianId, Integer departmentId);

 
}

