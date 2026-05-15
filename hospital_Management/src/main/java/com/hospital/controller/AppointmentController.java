package com.hospital.controller;

import com.hospital.entity.Appointment;
import com.hospital.entity.Nurse;
import com.hospital.entity.Patient;
import com.hospital.entity.Physician;
import com.hospital.exception.AlreadyExistsException;
import com.hospital.exception.BadRequestException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.projection.AppointmentProjection;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.NurseRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.PhysicianRepository;
import com.hospital.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;


@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired private AppointmentService appointmentService;
    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private PhysicianRepository physicianRepository;
    @Autowired private NurseRepository nurseRepository;

    @GetMapping
    public ResponseEntity<Page<AppointmentProjection>> getAll(@PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(appointmentRepository.findAllProjectedBy(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentProjection> getById(@PathVariable Integer id) {
        return appointmentRepository.findProjectedByAppointmentId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AppointmentProjection> create(@RequestBody Map<String, Object> body) {
        Integer apptId = parseIntField(body, "appointmentId", true);
        if (apptId != null && appointmentService.existsById(apptId))
            throw new AlreadyExistsException("Appointment", apptId);

        Appointment saved = appointmentService.save(fromMap(null, body));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appointmentRepository.findProjectedByAppointmentId(saved.getAppointmentId()).orElseThrow());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentProjection> update(@PathVariable Integer id,
                                                        @RequestBody Map<String, Object> body) {
        appointmentService.getById(id); // 404 if not found
        appointmentService.save(fromMap(id, body));
        return ResponseEntity.ok(appointmentRepository.findProjectedByAppointmentId(id).orElseThrow());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        appointmentService.delete(id);
        return ResponseEntity.ok("Appointment " + id + " deleted successfully.");
    }

    // -----------------------------------------------------------------------
    //  Private helpers
    // -----------------------------------------------------------------------

    private Appointment fromMap(Integer idOverride, Map<String, Object> body) {
        Appointment a = new Appointment();
        Integer apptId = idOverride != null ? idOverride : parseIntField(body, "appointmentId", false);
        a.setAppointmentId(apptId);

        Integer ssn = parseIntField(body, "patientSsn", true);
        if (ssn == null) throw new BadRequestException("Incorrect data: 'patientSsn' is required.");
        Patient patient = patientRepository.findById(ssn)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "SSN", ssn));
        a.setPatient(patient);

        Integer pid = parseIntField(body, "physicianId", true);
        if (pid == null) throw new BadRequestException("Incorrect data: 'physicianId' is required.");
        Physician ph = physicianRepository.findById(pid)
                .orElseThrow(() -> new ResourceNotFoundException("Physician", "ID", pid));
        a.setPhysician(ph);

        if (body.get("prepNurseId") != null && !body.get("prepNurseId").toString().isBlank()) {
            Integer nid = parseIntField(body, "prepNurseId", true);
            Nurse nurse = nurseRepository.findById(nid)
                    .orElseThrow(() -> new ResourceNotFoundException("Nurse", "ID", nid));
            a.setPrepNurse(nurse);
        }

        if (body.get("start") == null || body.get("start").toString().isBlank())
            throw new BadRequestException("Incorrect data: 'start' date-time is required.");
        a.setStart(parseDateTime(body.get("start").toString(), "start"));

        if (body.get("end") == null || body.get("end").toString().isBlank())
            throw new BadRequestException("Incorrect data: 'end' date-time is required.");
        a.setEnd(parseDateTime(body.get("end").toString(), "end"));

        if (body.get("examinationRoom") == null || body.get("examinationRoom").toString().isBlank())
            throw new BadRequestException("Incorrect data: 'examinationRoom' is required.");
        a.setExaminationRoom(body.get("examinationRoom").toString());

        return a;
    }

    private Integer parseIntField(Map<String, Object> body, String field, boolean required) {
        Object val = body.get(field);
        if (val == null || val.toString().isBlank()) return null;
        try {
            return Integer.parseInt(val.toString().trim());
        } catch (NumberFormatException e) {
            throw new BadRequestException(
                    "Incorrect data: '" + field + "' must be a valid integer, got: " + val);
        }
    }

    private LocalDateTime parseDateTime(String raw, String fieldName) {
        try {
            return LocalDateTime.parse(raw.replace(" ", "T"));
        } catch (DateTimeParseException e) {
            throw new BadRequestException(
                    "Incorrect data: '" + fieldName + "' must be in ISO format (yyyy-MM-ddTHH:mm), got: " + raw);
        }
    }
}
