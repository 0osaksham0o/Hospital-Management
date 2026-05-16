package com.hospital.service.impl;

import com.hospital.entity.Physician;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.PhysicianRepository;
import com.hospital.service.PhysicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhysicianServiceImpl implements PhysicianService {

    @Autowired private PhysicianRepository repo;
    @Autowired private DepartmentRepository departmentRepo;

    @Override public List<Physician> getAll()                            { return repo.findAll(); }
    @Override public Page<Physician> getAll(Pageable p)                  { return repo.findAll(p); }
    @Override public Physician getById(Integer id)                       { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Physician not found: " + id)); }
    @Override public boolean existsById(Integer id)                      { return repo.existsById(id); }
    @Override public Physician save(Physician p)                         { return repo.save(p); }
    @Override public void delete(Integer id)                             { getById(id); repo.deleteById(id); }

    @Override public Optional<Physician> getByName(String name)          { return repo.findByName(name); }
    @Override public List<Physician> searchByName(String part)           { return repo.findByNameContainingIgnoreCase(part); }

    @Override public List<Physician> getByPosition(String pos)           { return repo.findByPosition(pos); }
    @Override public List<Physician> searchByPosition(String kw)         { return repo.findByPositionContainingIgnoreCase(kw); }

    @Override public Optional<Physician> getBySsn(Integer ssn)           { return repo.findBySsn(ssn); }
    @Override public boolean existsBySsn(Integer ssn)                    { return repo.existsBySsn(ssn); }

    @Override public List<Physician> getAllOrderedByName()                { return repo.findAllByOrderByNameAsc(); }
    @Override public List<Physician> getDepartmentHeads() {
        return departmentRepo.findAll().stream()
                .map(d -> d.getHead())
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }
}
