package com.hospital.service;

import com.hospital.entity.Procedure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProcedureService {
    List<Procedure> getAll();
    Page<Procedure> getAll(Pageable pageable);
    Procedure getById(Integer code);
    boolean existsById(Integer code);
    Procedure save(Procedure procedure);
    void delete(Integer code);

    // By Name
    Optional<Procedure> getByName(String name);
    List<Procedure> searchByName(String keyword);
    boolean existsByName(String name);

    // By Cost
    List<Procedure> getByCostLessThan(Double max);
    List<Procedure> getByCostGreaterThan(Double min);
    List<Procedure> getByCostBetween(Double min, Double max);

    // Sorted
    List<Procedure> getAllOrderedByCost();
    List<Procedure> getAllOrderedByName();
}
