package com.hospital.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.hospital.entity.Procedure;

@RepositoryRestResource(exported = false)
public interface ProcedureRepository extends JpaRepository<Procedure, Integer> {

    
   

    /** Find procedures whose name contains a keyword (case-insensitive). */
    List<Procedure> findByNameContainingIgnoreCase(String keyword);



    List<Procedure> findByCostLessThan(Double maxCost);



    List<Procedure> findByCostBetween(Double minCost, Double maxCost);

    
}

