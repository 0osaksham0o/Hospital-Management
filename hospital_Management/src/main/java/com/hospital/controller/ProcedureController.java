package com.hospital.controller;

import com.hospital.entity.Procedure;
import com.hospital.exception.AlreadyExistsException;
import com.hospital.exception.BadRequestException;
import com.hospital.projection.ProcedureProjection;
import com.hospital.repository.ProcedureRepository;
import com.hospital.service.ProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Procedure — full CRUD.
 * GET endpoints return ProcedureProjection (flat scalars).
 */
@RestController
@RequestMapping("/api/procedures")
@CrossOrigin(origins = "*")
public class ProcedureController {

    @Autowired private ProcedureService procedureService;
    @Autowired private ProcedureRepository procedureRepository;

    @GetMapping
    public ResponseEntity<Page<ProcedureProjection>> getAll(@PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(procedureRepository.findAllProjectedBy(pageable));
    }

    @GetMapping("/{code}")
    public ResponseEntity<ProcedureProjection> getById(@PathVariable Integer code) {
        return procedureRepository.findProjectedByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProcedureProjection> create(@RequestBody Procedure procedure) {
        if (procedure.getCode() == null)
            throw new BadRequestException("Incorrect data: 'code' is required.");
        if (procedure.getName() == null || procedure.getName().isBlank())
            throw new BadRequestException("Incorrect data: 'name' is required.");
        if (procedureService.existsById(procedure.getCode()))
            throw new AlreadyExistsException("Procedure", procedure.getCode());

        procedureService.save(procedure);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(procedureRepository.findProjectedByCode(procedure.getCode()).orElseThrow());
    }

    @PutMapping("/{code}")
    public ResponseEntity<ProcedureProjection> update(@PathVariable Integer code,
                                                       @RequestBody Procedure procedure) {
        procedureService.getById(code); // 404 if not found
        procedure.setCode(code);
        procedureService.save(procedure);
        return ResponseEntity.ok(procedureRepository.findProjectedByCode(code).orElseThrow());
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<String> delete(@PathVariable Integer code) {
        procedureService.delete(code);
        return ResponseEntity.ok("Procedure " + code + " deleted successfully.");
    }
}
