package com.hospital.service.impl;

import com.hospital.entity.Procedure;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.ProcedureRepository;
import com.hospital.service.ProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProcedureServiceImpl implements ProcedureService {

    @Autowired
    private ProcedureRepository repo;

    @Override public List<Procedure> getAll()                            { return repo.findAll(); }
    @Override public Page<Procedure> getAll(Pageable p)                  { return repo.findAll(p); }
    @Override public Procedure getById(Integer code)                     { return repo.findById(code).orElseThrow(() -> new ResourceNotFoundException("Procedure not found: " + code)); }
    @Override public boolean existsById(Integer code)                    { return repo.existsById(code); }
    @Override public Procedure save(Procedure proc)                      { return repo.save(proc); }
    @Override public void delete(Integer code)                           { getById(code); repo.deleteById(code); }

    @Override public Optional<Procedure> getByName(String name)          { return repo.findByName(name); }
    @Override public List<Procedure> searchByName(String kw)             { return repo.findByNameContainingIgnoreCase(kw); }
    @Override public boolean existsByName(String name)                   { return repo.existsByName(name); }

    @Override public List<Procedure> getByCostLessThan(Double max)       { return repo.findByCostLessThan(max); }
    @Override public List<Procedure> getByCostGreaterThan(Double min)    { return repo.findByCostGreaterThan(min); }
    @Override public List<Procedure> getByCostBetween(Double min, Double max) { return repo.findByCostBetween(min, max); }

    @Override public List<Procedure> getAllOrderedByCost()               { return repo.findAllByOrderByCostAsc(); }
    @Override public List<Procedure> getAllOrderedByName()               { return repo.findAllByOrderByNameAsc(); }
}
