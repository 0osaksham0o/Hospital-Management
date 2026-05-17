package com.hospital.controller;

import com.hospital.entity.Physician;
import com.hospital.exception.AlreadyExistsException;
import com.hospital.exception.BadRequestException;
import com.hospital.projection.PhysicianProjection;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.PhysicianRepository;
import com.hospital.service.AppointmentService;
import com.hospital.service.PhysicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Physician.
 * GET endpoints return PhysicianProjection (flat IDs, no entity graph).
 * POST / PUT accept the full entity JSON.
 */
@RestController
@RequestMapping("/api/physicians")
@CrossOrigin(origins = "*")
public class PhysicianController {

    @Autowired private PhysicianService physicianService;
    @Autowired private PhysicianRepository physicianRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<Page<PhysicianProjection>> getAll(@PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(physicianRepository.findAllProjectedBy(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PhysicianProjection>> search(
            @RequestParam(defaultValue = "") String name,
            @PageableDefault(size = 5) Pageable pageable) {
        if (name.isBlank()) return ResponseEntity.ok(physicianRepository.findAllProjectedBy(pageable));
        return ResponseEntity.ok(physicianRepository.findByNameContainingIgnoreCase(name, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhysicianProjection> getById(@PathVariable Integer id) {
        return physicianRepository.findProjectedByEmployeeId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PhysicianProjection> create(@RequestBody Physician physician) {
        if (physician.getEmployeeId() == null)
            throw new BadRequestException("Incorrect data: 'employeeId' is required.");
        if (physician.getName() == null || physician.getName().isBlank())
            throw new BadRequestException("Incorrect data: 'name' is required.");
        if (physicianService.existsById(physician.getEmployeeId()))
            throw new AlreadyExistsException("Physician", physician.getEmployeeId());

        physicianService.save(physician);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(physicianRepository.findProjectedByEmployeeId(physician.getEmployeeId()).orElseThrow());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhysicianProjection> update(@PathVariable Integer id,
                                                       @RequestBody Physician physician) {
        physicianService.getById(id); // 404 if not found
        physician.setEmployeeId(id);
        physicianService.save(physician);
        return ResponseEntity.ok(physicianRepository.findProjectedByEmployeeId(id).orElseThrow());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        physicianService.getById(id); // 404 if not found

        // Guard: block if physician is primary care physician for any patient
        long patientCount = patientRepository.countByPrimaryCarePhysician_EmployeeId(id);
        if (patientCount > 0)
            throw new BadRequestException(
                    "Cannot delete Physician (ID: " + id + "): " + patientCount
                    + " patient(s) have this physician as their primary care doctor. "
                    + "Please reassign those patients first.");

        // Guard: block if physician has booked appointments
        long apptCount = appointmentService.countByPhysician(id);
        if (apptCount > 0)
            throw new BadRequestException(
                    "Cannot delete Physician (ID: " + id + "): physician has "
                    + apptCount + " booked appointment(s). "
                    + "Please cancel those appointments first.");

        physicianService.delete(id);
        return ResponseEntity.ok("Physician " + id + " deleted successfully.");
    }
}
