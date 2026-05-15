package com.hospital.controller;

import com.hospital.entity.*;
import com.hospital.projection.PrescriptionProjection;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;


@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(origins = "*")
public class PrescriptionController {

    @Autowired private PrescriptionRepository prescriptionRepository;
    @Autowired private AppointmentRepository appointmentRepository;

    @GetMapping
    public ResponseEntity<Page<PrescriptionProjection>> getAll(@PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(prescriptionRepository.findAllProjectedBy(pageable));
    }

    @GetMapping("/{physicianId}/{patientSsn}/{medicationCode}")
    public ResponseEntity<PrescriptionProjection> getById(@PathVariable Integer physicianId,
                                                          @PathVariable Integer patientSsn,
                                                          @PathVariable Integer medicationCode) {
        PrescriptionId id = new PrescriptionId(physicianId, patientSsn, medicationCode);
        return prescriptionRepository.findProjectedById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PrescriptionProjection> create(@RequestBody Map<String, Object> body) {
        Prescription saved = prescriptionRepository.save(toEntity(body));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(prescriptionRepository.findProjectedById(saved.getId()).orElseThrow());
    }

    @PutMapping("/{physicianId}/{patientSsn}/{medicationCode}")
    public ResponseEntity<PrescriptionProjection> update(@PathVariable Integer physicianId,
                                                         @PathVariable Integer patientSsn,
                                                         @PathVariable Integer medicationCode,
                                                         @RequestBody Map<String, Object> body) {
        PrescriptionId id = new PrescriptionId(physicianId, patientSsn, medicationCode);
        if (!prescriptionRepository.existsById(id)) return ResponseEntity.notFound().build();
        body.put("physicianId", physicianId);
        body.put("patientSsn", patientSsn);
        body.put("medicationCode", medicationCode);
        prescriptionRepository.save(toEntity(body));
        return ResponseEntity.ok(prescriptionRepository.findProjectedById(id).orElseThrow());
    }

    @DeleteMapping("/{physicianId}/{patientSsn}/{medicationCode}")
    public ResponseEntity<String> delete(@PathVariable Integer physicianId,
                                         @PathVariable Integer patientSsn,
                                         @PathVariable Integer medicationCode) {
        PrescriptionId id = new PrescriptionId(physicianId, patientSsn, medicationCode);
        if (!prescriptionRepository.existsById(id)) return ResponseEntity.notFound().build();
        prescriptionRepository.deleteById(id);
        return ResponseEntity.ok("Prescription deleted successfully.");
    }


    private Prescription toEntity(Map<String, Object> body) {
        Integer physicianId   = Integer.parseInt(body.get("physicianId").toString());
        Integer patientSsn    = Integer.parseInt(body.get("patientSsn").toString());
        Integer medicationCode = Integer.parseInt(body.get("medicationCode").toString());

        PrescriptionId id = new PrescriptionId(physicianId, patientSsn, medicationCode);

        Physician physician = new Physician();
        physician.setEmployeeId(physicianId);

        Patient patient = new Patient();
        patient.setSsn(patientSsn);

        Medication medication = new Medication();
        medication.setCode(medicationCode);

        LocalDate date = body.get("date") != null
                ? LocalDate.parse(body.get("date").toString()) : null;
        String dose = body.get("dose") != null ? body.get("dose").toString() : null;

        Appointment appointment = null;
        if (body.get("appointmentId") != null && !body.get("appointmentId").toString().isBlank()) {
            Integer apptId = Integer.parseInt(body.get("appointmentId").toString());
            appointment = appointmentRepository.findById(apptId).orElse(null);
        }

        return new Prescription(id, physician, patient, medication, date, appointment, dose);
    }
}
