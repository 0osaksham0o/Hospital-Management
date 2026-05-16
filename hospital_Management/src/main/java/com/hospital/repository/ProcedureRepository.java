package com.hospital.repository;

import com.hospital.entity.Procedure;
import com.hospital.projection.ProcedureProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface ProcedureRepository extends JpaRepository<Procedure, Integer> {
    Page<ProcedureProjection> findAllProjectedBy(Pageable pageable);
    Optional<ProcedureProjection> findProjectedByCode(Integer code);
    Optional<Procedure> findByName(String name);
    List<Procedure> findByNameContainingIgnoreCase(String keyword);

    boolean existsByName(String name);
    List<Procedure> findByCostLessThan(Double maxCost);

    List<Procedure> findByCostGreaterThan(Double minCost);
    List<Procedure> findByCostBetween(Double minCost, Double maxCost);

    List<Procedure> findAllByOrderByCostAsc();
    List<Procedure> findAllByOrderByNameAsc();
}
