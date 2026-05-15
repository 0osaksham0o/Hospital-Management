package com.hospital.controller;

import com.hospital.entity.Department;
import com.hospital.entity.Physician;
import com.hospital.exception.AlreadyExistsException;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.projection.DepartmentProjection;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.PhysicianRepository;
import com.hospital.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for Department — full CRUD.
 * GET endpoints return DepartmentProjection (flat headId, no entity graph).
 * Write body: { "departmentId":1, "name":"...", "headId":1 }
 */
@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "*")
public class DepartmentController {

    @Autowired private DepartmentService departmentService;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private PhysicianRepository physicianRepository;

    @GetMapping
    public ResponseEntity<Page<DepartmentProjection>> getAll(@PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(departmentRepository.findAllProjectedBy(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentProjection> getById(@PathVariable Integer id) {
        return departmentRepository.findProjectedByDepartmentId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DepartmentProjection> create(@RequestBody Map<String, Object> body) {
        Integer deptId = parseIntField(body, "departmentId", true);
        if (deptId != null && departmentService.existsById(deptId))
            throw new AlreadyExistsException("Department", deptId);

        Department saved = departmentService.save(fromMap(null, body));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(departmentRepository.findProjectedByDepartmentId(saved.getDepartmentId()).orElseThrow());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentProjection> update(@PathVariable Integer id,
                                                        @RequestBody Map<String, Object> body) {
        departmentService.getById(id); // 404 if not found
        departmentService.save(fromMap(id, body));
        return ResponseEntity.ok(departmentRepository.findProjectedByDepartmentId(id).orElseThrow());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        departmentService.delete(id);
        return ResponseEntity.ok("Department " + id + " deleted successfully.");
    }


    private Department fromMap(Integer idOverride, Map<String, Object> body) {
        Department d = new Department();
        Integer did = idOverride != null ? idOverride : parseIntField(body, "departmentId", false);
        d.setDepartmentId(did);

        if (body.get("name") == null || body.get("name").toString().isBlank())
            throw new BadRequestException("Incorrect data: 'name' is required.");
        d.setName(body.get("name").toString().trim());

        Integer headId = parseIntField(body, "headId", true);
        if (headId == null) throw new BadRequestException("Incorrect data: 'headId' is required.");
        Physician head = physicianRepository.findById(headId)
                .orElseThrow(() -> new ResourceNotFoundException("Physician", "ID", headId));
        d.setHead(head);
        return d;
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

