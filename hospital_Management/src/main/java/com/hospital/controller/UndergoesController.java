package com.hospital.controller;

import com.hospital.entity.Undergoes;
import com.hospital.entity.UndergoesId;
import com.hospital.projection.UndergoesProjection;
import com.hospital.repository.UndergoesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/undergoes")
@CrossOrigin(origins = "*")
public class UndergoesController {

    private final UndergoesRepository undergoesRepository;

    public UndergoesController(UndergoesRepository undergoesRepository) {
        this.undergoesRepository = undergoesRepository;
    }

    @GetMapping
    public ResponseEntity<Page<UndergoesProjection>> getAllUndergoes(
            @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(undergoesRepository.findAllProjectedBy(pageable));
    }

    @GetMapping("/{patientSsn}/{procedureCode}/{stayId}")
    public ResponseEntity<UndergoesProjection> getUndergoesById(
            @PathVariable Integer patientSsn,
            @PathVariable Integer procedureCode,
            @PathVariable Integer stayId) {
        UndergoesId id = new UndergoesId(patientSsn, procedureCode, stayId);
        return undergoesRepository.findProjectedById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UndergoesProjection> createUndergoes(@RequestBody Undergoes undergoes) {
        Undergoes saved = undergoesRepository.save(undergoes);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(undergoesRepository.findProjectedById(saved.getId()).orElseThrow());
    }

    @PutMapping("/{patientSsn}/{procedureCode}/{stayId}")
    public ResponseEntity<UndergoesProjection> updateUndergoes(
            @PathVariable Integer patientSsn,
            @PathVariable Integer procedureCode,
            @PathVariable Integer stayId,
            @RequestBody Undergoes updatedUndergoes) {
        UndergoesId id = new UndergoesId(patientSsn, procedureCode, stayId);
        if (!undergoesRepository.existsById(id)) return ResponseEntity.notFound().build();
        updatedUndergoes.setId(id);
        undergoesRepository.save(updatedUndergoes);
        return ResponseEntity.ok(undergoesRepository.findProjectedById(id).orElseThrow());
    }

    @DeleteMapping("/{patientSsn}/{procedureCode}/{stayId}")
    public ResponseEntity<String> deleteUndergoes(
            @PathVariable Integer patientSsn,
            @PathVariable Integer procedureCode,
            @PathVariable Integer stayId) {
        UndergoesId id = new UndergoesId(patientSsn, procedureCode, stayId);
        if (!undergoesRepository.existsById(id)) return ResponseEntity.notFound().build();
        undergoesRepository.deleteById(id);
        return ResponseEntity.ok("Undergoes record [patient=" + patientSsn +
                ", procedure=" + procedureCode + ", stay=" + stayId + "] deleted successfully.");
    }
}
