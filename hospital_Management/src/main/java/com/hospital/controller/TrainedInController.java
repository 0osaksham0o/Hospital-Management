package com.hospital.controller;

import com.hospital.entity.*;
import com.hospital.projection.TrainedInProjection;
import com.hospital.repository.PhysicianRepository;
import com.hospital.repository.ProcedureRepository;
import com.hospital.repository.TrainedInRepository;
import com.hospital.service.TrainedInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;


@RestController
@RequestMapping("/api/trainedin")
@CrossOrigin(origins = "*")
public class TrainedInController {

    @Autowired private TrainedInService trainedInService;
    @Autowired private TrainedInRepository trainedInRepository;
    @Autowired private PhysicianRepository physicianRepository;
    @Autowired private ProcedureRepository procedureRepository;

    @GetMapping
    public ResponseEntity<Page<TrainedInProjection>> getAll(@PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(trainedInRepository.findAllProjectedBy(pageable));
    }

    @GetMapping("/{physicianId}/{treatmentCode}")
    public ResponseEntity<TrainedInProjection> getById(@PathVariable Integer physicianId,
                                                       @PathVariable Integer treatmentCode) {
        TrainedInId id = new TrainedInId(physicianId, treatmentCode);
        return trainedInRepository.findProjectedById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TrainedInProjection> create(@RequestBody Map<String, Object> body) {
        TrainedIn saved = trainedInService.save(toEntity(body));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(trainedInRepository.findProjectedById(saved.getId()).orElseThrow());
    }

    @PutMapping("/{physicianId}/{treatmentCode}")
    public ResponseEntity<TrainedInProjection> update(@PathVariable Integer physicianId,
                                                      @PathVariable Integer treatmentCode,
                                                      @RequestBody Map<String, Object> body) {
        trainedInService.getById(new TrainedInId(physicianId, treatmentCode)); // 404
        body.put("physicianId", physicianId);
        body.put("treatmentCode", treatmentCode);
        TrainedIn saved = trainedInService.save(toEntity(body));
        return ResponseEntity.ok(trainedInRepository.findProjectedById(saved.getId()).orElseThrow());
    }

    @DeleteMapping("/{physicianId}/{treatmentCode}")
    public ResponseEntity<String> delete(@PathVariable Integer physicianId,
                                         @PathVariable Integer treatmentCode) {
        trainedInService.delete(new TrainedInId(physicianId, treatmentCode));
        return ResponseEntity.ok("Certification deleted.");
    }

    // -----------------------------------------------------------------------
    //  Private helper
    // -----------------------------------------------------------------------

    private TrainedIn toEntity(Map<String, Object> body) {
        Integer physicianId   = Integer.parseInt(body.get("physicianId").toString());
        Integer treatmentCode = Integer.parseInt(body.get("treatmentCode").toString());

        TrainedInId id = new TrainedInId(physicianId, treatmentCode);
        Physician physician = physicianRepository.findById(physicianId)
                .orElseThrow(() -> new RuntimeException("Physician not found: " + physicianId));
        Procedure procedure = procedureRepository.findById(treatmentCode)
                .orElseThrow(() -> new RuntimeException("Procedure not found: " + treatmentCode));

        LocalDateTime certDate = body.get("certificationDate") != null
                ? LocalDateTime.parse(body.get("certificationDate").toString().replace(" ", "T")) : null;
        LocalDateTime certExpires = body.get("certificationExpires") != null
                ? LocalDateTime.parse(body.get("certificationExpires").toString().replace(" ", "T")) : null;

        return new TrainedIn(id, physician, procedure, certDate, certExpires);
    }
}
