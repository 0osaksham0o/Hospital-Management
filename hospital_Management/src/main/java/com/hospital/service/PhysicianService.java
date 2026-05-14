package com.hospital.service;

import com.hospital.entity.Physician;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PhysicianService {
    List<Physician> getAll();
    Page<Physician> getAll(Pageable pageable);
    Physician getById(Integer id);
    boolean existsById(Integer id);
    Physician save(Physician physician);
    void delete(Integer id);
}
