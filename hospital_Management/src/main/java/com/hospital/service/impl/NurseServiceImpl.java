package com.hospital.service.impl;

import com.hospital.entity.Nurse;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.NurseRepository;
import com.hospital.service.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NurseServiceImpl implements NurseService {

    @Autowired
    private NurseRepository repo;

    @Override public List<Nurse> getAll()                                { return repo.findAll(); }
    @Override public Page<Nurse> getAll(Pageable p)                      { return repo.findAll(p); }
    @Override public Nurse getById(Integer id)                           { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nurse not found: " + id)); }
    @Override public boolean existsById(Integer id)                      { return repo.existsById(id); }
    @Override public Nurse save(Nurse n)                                 { return repo.save(n); }
    @Override public void delete(Integer id)                             { getById(id); repo.deleteById(id); }

    @Override public Optional<Nurse> getByName(String name)             { return repo.findByName(name); }
    @Override public List<Nurse> searchByName(String part)              { return repo.findByNameContainingIgnoreCase(part); }

    @Override public List<Nurse> getByPosition(String pos)              { return repo.findByPosition(pos); }

    @Override public List<Nurse> getByRegistered(Boolean reg)           { return repo.findByRegistered(reg); }
    @Override public long countByRegistered(Boolean reg)                { return repo.countByRegistered(reg); }

    @Override public Optional<Nurse> getBySsn(Integer ssn)             { return repo.findBySsn(ssn); }
    @Override public boolean existsBySsn(Integer ssn)                  { return repo.existsBySsn(ssn); }

    @Override public List<Nurse> getAllRegisteredOrderedByName()        { return repo.findByRegisteredTrueOrderByNameAsc(); }
}
