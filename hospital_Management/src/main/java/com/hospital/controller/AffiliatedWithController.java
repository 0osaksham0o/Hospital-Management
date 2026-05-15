package com.hospital.controller;
import com.hospital.entity.*;
import com.hospital.projection.AffiliatedWithProjection;
import com.hospital.repository.AffiliatedWithRepository;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.PhysicianRepository;
import com.hospital.service.AffiliatedWithService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for AffiliatedWith — full CRUD.
 * GET endpoints return AffiliatedWithProjection (flat IDs).
 * Write body: { "physicianId":1, "departmentId":2, "primaryAffiliation":true }
 */
@RestController
@RequestMapping("/api/affiliations")
@CrossOrigin(origins = "*")
public class AffiliatedWithController {

    @Autowired private AffiliatedWithService affiliatedWithService;
    @Autowired private AffiliatedWithRepository affiliatedWithRepository;
    @Autowired private PhysicianRepository physicianRepository;
    @Autowired private DepartmentRepository departmentRepository;

    @GetMapping
    public ResponseEntity<Page<AffiliatedWithProjection>> getAll(@PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(affiliatedWithRepository.findAllProjectedBy(pageable));
    }

    @GetMapping("/{physicianId}/{departmentId}")
    public ResponseEntity<AffiliatedWithProjection> getById(@PathVariable Integer physicianId,
                                                             @PathVariable Integer departmentId) {
        AffiliatedWithId id = new AffiliatedWithId(physicianId, departmentId);
        return affiliatedWithRepository.findProjectedById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AffiliatedWithProjection> create(@RequestBody Map<String, Object> body) {
        AffiliatedWith saved = affiliatedWithService.save(toEntity(body));
        AffiliatedWithId id = new AffiliatedWithId(
                saved.getId().getPhysicianId(), saved.getId().getDepartmentId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(affiliatedWithRepository.findProjectedById(id).orElseThrow());
    }

    @PutMapping("/{physicianId}/{departmentId}")
    public ResponseEntity<AffiliatedWithProjection> update(@PathVariable Integer physicianId,
                                                            @PathVariable Integer departmentId,
                                                            @RequestBody Map<String, Object> body) {
        affiliatedWithService.getById(new AffiliatedWithId(physicianId, departmentId)); // 404
        body.put("physicianId", physicianId);
        body.put("departmentId", departmentId);
        affiliatedWithService.save(toEntity(body));
        return ResponseEntity.ok(
                affiliatedWithRepository.findProjectedById(
                        new AffiliatedWithId(physicianId, departmentId)).orElseThrow());
    }

    @DeleteMapping("/{physicianId}/{departmentId}")
    public ResponseEntity<String> delete(@PathVariable Integer physicianId,
                                          @PathVariable Integer departmentId) {
        affiliatedWithService.delete(new AffiliatedWithId(physicianId, departmentId));
        return ResponseEntity.ok("Affiliation deleted.");
    }

    // -----------------------------------------------------------------------
    //  Private helper
    // -----------------------------------------------------------------------

    private AffiliatedWith toEntity(Map<String, Object> body) {
        Integer physicianId  = Integer.parseInt(body.get("physicianId").toString());
        Integer departmentId = Integer.parseInt(body.get("departmentId").toString());
        Boolean primary = body.get("primaryAffiliation") != null
                && Boolean.parseBoolean(body.get("primaryAffiliation").toString());

        AffiliatedWithId id = new AffiliatedWithId(physicianId, departmentId);
        Physician physician = physicianRepository.findById(physicianId)
                .orElseThrow(() -> new RuntimeException("Physician not found: " + physicianId));
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found: " + departmentId));
        return new AffiliatedWith(id, physician, department, primary);
    }
}
