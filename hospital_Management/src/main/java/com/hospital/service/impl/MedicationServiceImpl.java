package com.hospital.service.impl;

import com.hospital.entity.Medication;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.MedicationRepository;
import com.hospital.service.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicationServiceImpl implements MedicationService {

    @Autowired
    private MedicationRepository repo;

    @Override public List<Medication> getAll()                           { return repo.findAll(); }
    @Override public Page<Medication> getAll(Pageable p)                 { return repo.findAll(p); }
    @Override public Medication getById(Integer code)                    { return repo.findById(code).orElseThrow(() -> new ResourceNotFoundException("Medication not found: " + code)); }
    @Override public boolean existsById(Integer code)                    { return repo.existsById(code); }
    @Override public Medication save(Medication m)                       { return repo.save(m); }
    @Override public void delete(Integer code)                           { getById(code); repo.deleteById(code); }

    @Override public Optional<Medication> getByName(String name)         { return repo.findByName(name); }
    @Override public List<Medication> searchByName(String kw)            { return repo.findByNameContainingIgnoreCase(kw); }
    @Override public boolean existsByName(String name)                   { return repo.existsByName(name); }

    @Override public List<Medication> getByBrand(String brand)           { return repo.findByBrand(brand); }
    @Override public List<Medication> searchByBrand(String kw)           { return repo.findByBrandContainingIgnoreCase(kw); }
    @Override public List<String> getAllDistinctBrands() {
        return repo.findAll().stream()
                .map(Medication::getBrand)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override public List<Medication> searchByDescription(String kw)     { return repo.findByDescriptionContainingIgnoreCase(kw); }

    @Override public List<Medication> getAllOrderedByName()               { return repo.findAllByOrderByNameAsc(); }
}
