package com.hospital.service.impl;

import com.hospital.entity.Physician;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.PhysicianRepository;
import com.hospital.service.PhysicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhysicianServiceImpl implements PhysicianService {

    @Autowired
    private PhysicianRepository physicianRepository;

    @Override
    public List<Physician> getAll() {
        return physicianRepository.findAll();
    }

    @Override
    public Page<Physician> getAll(Pageable pageable) {
        return physicianRepository.findAll(pageable);
    }

    @Override
    public Physician getById(Integer id) {
        return physicianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Physician not found: " + id));
    }

    @Override
    public boolean existsById(Integer id) { return physicianRepository.existsById(id); }

    @Override
    public Physician save(Physician physician) {
        return physicianRepository.save(physician);
    }

    @Override
    public void delete(Integer id) {
        getById(id);
        physicianRepository.deleteById(id);
    }
}

