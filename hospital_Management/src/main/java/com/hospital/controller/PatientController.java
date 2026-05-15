package com.hospital.controller;

import com.hospital.entity.Patient;
import com.hospital.entity.Physician;
import com.hospital.exception.AlreadyExistsException;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.projection.PatientProjection;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.PhysicianRepository;
import com.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for Patient — full CRUD.
 * GET endpoints return PatientProjection (flat IDs, no entity graph).
 * Write body: { "ssn":101, "name":"...", "address":"...", "phone":"...", "insuranceId":5555, "pcpId":1 }
 */
@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    @Autowired private PatientService patientService;
    @Autowired private PatientRepository patientRepository;
    @Autowired private PhysicianRepository physicianRepository;

    @GetMapping
    public ResponseEntity<Page<PatientProjection>> getAll(@PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(patientRepository.findAllProjectedBy(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PatientProjection>> search(
            @RequestParam(defaultValue = "") String name,
            @PageableDefault(size = 5) Pageable pageable) {
        if (name.isBlank()) return ResponseEntity.ok(patientRepository.findAllProjectedBy(pageable));
        return ResponseEntity.ok(patientRepository.findByNameContainingIgnoreCase(name, pageable));
    }

    @GetMapping("/{ssn}")
    public ResponseEntity<PatientProjection> getById(@PathVariable Integer ssn) {
        return patientRepository.findProjectedBySsn(ssn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PatientProjection> create(@RequestBody Map<String, Object> body) {
        Integer ssn = parseIntField(body, "ssn", true);
        if (ssn == null) throw new BadRequestException("Incorrect data: 'ssn' is required.");
        if (patientService.existsById(ssn))
            throw new AlreadyExistsException("Patient", ssn);

        patientService.save(fromMap(null, body));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(patientRepository.findProjectedBySsn(ssn).orElseThrow());
    }

    @PutMapping("/{ssn}")
    public ResponseEntity<PatientProjection> update(@PathVariable Integer ssn,
                                                    @RequestBody Map<String, Object> body) {
        patientService.getById(ssn); // 404 if not found
        patientService.save(fromMap(ssn, body));
        return ResponseEntity.ok(patientRepository.findProjectedBySsn(ssn).orElseThrow());
    }

    @DeleteMapping("/{ssn}")
    public ResponseEntity<String> delete(@PathVariable Integer ssn) {
        patientService.delete(ssn);
        return ResponseEntity.ok("Patient " + ssn + " deleted successfully.");
    }

    // -----------------------------------------------------------------------
    //  Private helpers
    // -----------------------------------------------------------------------

    private Patient fromMap(Integer ssnOverride, Map<String, Object> body) {
        Patient p = new Patient();
        Integer ssn = ssnOverride != null ? ssnOverride : parseIntField(body, "ssn", true);
        p.setSsn(ssn);

        if (body.get("name") == null || body.get("name").toString().isBlank())
            throw new BadRequestException("Incorrect data: 'name' is required.");
        p.setName(body.get("name").toString().trim());

        p.setAddress(body.get("address") != null ? body.get("address").toString() : null);
        p.setPhone(body.get("phone") != null ? body.get("phone").toString() : null);

        if (body.get("insuranceId") != null)
            p.setInsuranceId(parseIntField(body, "insuranceId", true));

        if (body.get("pcpId") != null) {
            Integer pcpId = parseIntField(body, "pcpId", true);
            Physician pcp = physicianRepository.findById(pcpId)
                    .orElseThrow(() -> new ResourceNotFoundException("Physician", "ID", pcpId));
            p.setPrimaryCarePhysician(pcp);
        }
        return p;
    }

    private Integer parseIntField(Map<String, Object> body, String field, boolean strict) {
        Object val = body.get(field);
        if (val == null || val.toString().isBlank()) return null;
        try {
            return Integer.parseInt(val.toString().trim());
        } catch (NumberFormatException e) {
            throw new BadRequestException(
                    "Incorrect data: '" + field + "' must be a valid integer, got: " + val);
        }
    }
}
