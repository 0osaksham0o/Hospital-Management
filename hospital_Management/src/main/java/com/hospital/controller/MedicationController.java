package com.hospital.controller;

import com.hospital.entity.Medication;
import com.hospital.exception.AlreadyExistsException;
import com.hospital.exception.BadRequestException;
import com.hospital.projection.MedicationProjection;
import com.hospital.repository.MedicationRepository;
import com.hospital.service.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/medications")
@CrossOrigin(origins = "*")
public class MedicationController {

    @Autowired private MedicationService medicationService;
    @Autowired private MedicationRepository medicationRepository;

    @GetMapping
    public ResponseEntity<Page<MedicationProjection>> getAll(@PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(medicationRepository.findAllProjectedBy(pageable));
    }

    @GetMapping("/{code}")
    public ResponseEntity<MedicationProjection> getById(@PathVariable Integer code) {
        return medicationRepository.findProjectedByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MedicationProjection> create(@RequestBody Medication medication) {
        if (medication.getCode() != null && medicationService.existsById(medication.getCode()))
            throw new AlreadyExistsException("Medication", medication.getCode());
        if (medication.getName() == null || medication.getName().isBlank())
            throw new BadRequestException("Incorrect data: 'name' is required.");
        if (medication.getBrand() == null || medication.getBrand().isBlank())
            throw new BadRequestException("Incorrect data: 'brand' is required.");
        medicationService.save(medication);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(medicationRepository.findProjectedByCode(medication.getCode()).orElseThrow());
    }

    @PutMapping("/{code}")
    public ResponseEntity<MedicationProjection> update(@PathVariable Integer code,
                                                       @RequestBody Medication medication) {
        medicationService.getById(code); // 404 if not found
        medication.setCode(code);
        medicationService.save(medication);
        return ResponseEntity.ok(medicationRepository.findProjectedByCode(code).orElseThrow());
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<String> delete(@PathVariable Integer code) {
        medicationService.delete(code);
        return ResponseEntity.ok("Medication " + code + " deleted successfully.");
    }
}
