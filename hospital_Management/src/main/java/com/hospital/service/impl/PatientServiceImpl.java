package com.hospital.service.impl;

import com.hospital.entity.Patient;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.PatientRepository;
import com.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository repo;

    @Override
    public List<Patient> getAll() {
        return repo.findAll();
    }

    @Override
    public Page<Patient> getAll(Pageable p) {
        return repo.findAll(p);
    }

    @Override
    public Patient getById(Integer ssn) {
        return repo.findById(ssn).orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + ssn));
    }

    @Override
    public boolean existsById(Integer ssn) {
        return repo.existsById(ssn);
    }

    @Override
    public Patient save(Patient p) {
        return repo.save(p);
    }

    @Override
    public void delete(Integer ssn) {
        getById(ssn);
        repo.deleteById(ssn);
    }

    @Override
    public Optional<Patient> getByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public List<Patient> searchByName(String part) {
        return repo.findByNameContainingIgnoreCase(part);
    }

    @Override
    public boolean existsByName(String name) {
        return repo.existsByName(name);
    }

    @Override
    public Optional<Patient> getByPhone(String phone) {
        return repo.findByPhone(phone);
    }

    @Override
    public List<Patient> getByAddress(String address) {
        return repo.findByAddress(address);
    }

    @Override
    public Optional<Patient> getByInsuranceId(Integer id) {
        return repo.findByInsuranceId(id);
    }

    @Override
    public boolean existsByInsuranceId(Integer id) {
        return repo.existsByInsuranceId(id);
    }

    @Override
    public List<Patient> getByPcp(Integer physicianId) {
        return repo.findByPrimaryCarePhysician_EmployeeId(physicianId);
    }

    @Override
    public long countByPcp(Integer physicianId) {
        return repo.countByPrimaryCarePhysician_EmployeeId(physicianId);
    }

    @Override
    public List<Patient> getAllOrderedByName() {
        return repo.findAllByOrderByNameAsc();
    }
}
