package com.hospital.Controller;

import com.hospital.entity.Nurse;
import com.hospital.exception.AlreadyExistsException;
import com.hospital.exception.BadRequestException;
import com.hospital.projection.NurseProjection;
import com.hospital.repository.NurseRepository;
import com.hospital.service.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Nurse — full CRUD.
 * GET endpoints return NurseProjection (flat scalars, no entity graph).
 */
@RestController
@RequestMapping("/api/nurses")
@CrossOrigin(origins = "*")
public class NurseController {

    @Autowired private NurseService nurseService;
    @Autowired private NurseRepository nurseRepository;

    @GetMapping
    public ResponseEntity<Page<NurseProjection>> getAll(@PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(nurseRepository.findAllProjectedBy(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<NurseProjection>> search(
            @RequestParam(defaultValue = "") String name,
            @PageableDefault(size = 5) Pageable pageable) {
        if (name.isBlank()) return ResponseEntity.ok(nurseRepository.findAllProjectedBy(pageable));
        return ResponseEntity.ok(nurseRepository.findByNameContainingIgnoreCase(name, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NurseProjection> getById(@PathVariable Integer id) {
        return nurseRepository.findProjectedByEmployeeId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NurseProjection> create(@RequestBody Nurse nurse) {
        if (nurse.getEmployeeId() == null)
            throw new BadRequestException("Incorrect data: 'employeeId' is required.");
        if (nurse.getName() == null || nurse.getName().isBlank())
            throw new BadRequestException("Incorrect data: 'name' is required.");
        if (nurseService.existsById(nurse.getEmployeeId()))
            throw new AlreadyExistsException("Nurse", nurse.getEmployeeId());

        nurseService.save(nurse);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(nurseRepository.findProjectedByEmployeeId(nurse.getEmployeeId()).orElseThrow());
    }

    @PutMapping("/{id}")
    public ResponseEntity<NurseProjection> update(@PathVariable Integer id, @RequestBody Nurse nurse) {
        nurseService.getById(id); // 404 if not found
        nurse.setEmployeeId(id);
        nurseService.save(nurse);
        return ResponseEntity.ok(nurseRepository.findProjectedByEmployeeId(id).orElseThrow());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        nurseService.delete(id);
        return ResponseEntity.ok("Nurse " + id + " deleted successfully.");
    }
}
