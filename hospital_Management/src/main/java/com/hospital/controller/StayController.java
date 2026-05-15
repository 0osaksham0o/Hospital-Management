package com.hospital.controller;

import com.hospital.entity.Patient;
import com.hospital.entity.Room;
import com.hospital.entity.Stay;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.projection.StayProjection;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.RoomRepository;
import com.hospital.repository.StayRepository;
import com.hospital.service.StayService;
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
@RequestMapping("/api/stays")
@CrossOrigin(origins = "*")
public class StayController {

    @Autowired private StayService stayService;
    @Autowired private StayRepository stayRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private RoomRepository roomRepository;

    @GetMapping
    public ResponseEntity<Page<StayProjection>> getAll(@PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(stayRepository.findAllProjectedBy(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StayProjection> getById(@PathVariable Integer id) {
        return stayRepository.findProjectedByStayId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StayProjection> create(@RequestBody Map<String, Object> body) {
        Stay saved = stayService.save(fromMap(null, body));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stayRepository.findProjectedByStayId(saved.getStayId()).orElseThrow());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StayProjection> update(@PathVariable Integer id,
                                                 @RequestBody Map<String, Object> body) {
        stayService.getById(id); // 404 if not found
        stayService.save(fromMap(id, body));
        return ResponseEntity.ok(stayRepository.findProjectedByStayId(id).orElseThrow());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        stayService.delete(id);
        return ResponseEntity.ok().build();
    }



    private Stay fromMap(Integer idOverride, Map<String, Object> body) {
        Stay stay = new Stay();
        if (idOverride != null) stay.setStayId(idOverride);
        else if (body.get("stayId") != null)
            stay.setStayId(Integer.parseInt(body.get("stayId").toString()));

        if (body.get("patientSsn") != null) {
            Integer ssn = Integer.parseInt(body.get("patientSsn").toString());
            Patient patient = patientRepository.findById(ssn)
                    .orElseThrow(() -> new ResourceNotFoundException("Patient", "SSN", ssn));
            stay.setPatient(patient);
        }
        if (body.get("roomNumber") != null) {
            Integer roomNo = Integer.parseInt(body.get("roomNumber").toString());
            Room room = roomRepository.findById(roomNo)
                    .orElseThrow(() -> new ResourceNotFoundException("Room", "number", roomNo));
            stay.setRoom(room);
        }
        if (body.get("stayStart") != null)
            stay.setStayStart(parseDateTime(body.get("stayStart").toString()));
        if (body.get("stayEnd") != null)
            stay.setStayEnd(parseDateTime(body.get("stayEnd").toString()));
        return stay;
    }

    private LocalDateTime parseDateTime(String raw) {
        try {
            return LocalDateTime.parse(raw.replace(" ", "T"));
        } catch (DateTimeParseException e) {
            throw new com.hospital.exception.BadRequestException(
                    "Incorrect data: date-time must be in ISO format (yyyy-MM-ddTHH:mm), got: " + raw);
        }
    }
}
