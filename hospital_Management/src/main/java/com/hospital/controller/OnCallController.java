package com.hospital.Controller;

import com.hospital.entity.*;
import com.hospital.projection.OnCallProjection;
import com.hospital.repository.BlockRepository;
import com.hospital.repository.NurseRepository;
import com.hospital.repository.OnCallRepository;
import com.hospital.service.OnCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * REST controller for OnCall — full CRUD.
 * GET endpoints return OnCallProjection (flat IDs from embedded key).
 * Write body: { "nurseId":1, "blockFloor":1, "blockCode":2,
 *               "onCallStart":"2024-01-01T08:00", "onCallEnd":"2024-01-01T16:00" }
 */
@RestController
@RequestMapping("/api/oncalls")
@CrossOrigin(origins = "*")
public class OnCallController {

    @Autowired private OnCallService onCallService;
    @Autowired private OnCallRepository onCallRepository;
    @Autowired private NurseRepository nurseRepository;
    @Autowired private BlockRepository blockRepository;

    @GetMapping
    public ResponseEntity<Page<OnCallProjection>> getAll(@PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(onCallRepository.findAllProjectedBy(pageable));
    }

    @GetMapping("/{nurseId}/{floor}/{code}")
    public ResponseEntity<OnCallProjection> getById(@PathVariable Integer nurseId,
                                                     @PathVariable Integer floor,
                                                     @PathVariable Integer code) {
        OnCallId id = new OnCallId(nurseId, floor, code);
        return onCallRepository.findProjectedById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OnCallProjection> create(@RequestBody Map<String, Object> body) {
        OnCall saved = onCallService.save(toEntity(body));
        OnCallId id = new OnCallId(saved.getId().getNurseId(),
                saved.getId().getBlockFloor(), saved.getId().getBlockCode());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(onCallRepository.findProjectedById(id).orElseThrow());
    }

    @PutMapping("/{nurseId}/{floor}/{code}")
    public ResponseEntity<OnCallProjection> update(@PathVariable Integer nurseId,
                                                    @PathVariable Integer floor,
                                                    @PathVariable Integer code,
                                                    @RequestBody Map<String, Object> body) {
        onCallService.getById(new OnCallId(nurseId, floor, code)); // 404
        body.put("nurseId", nurseId);
        body.put("blockFloor", floor);
        body.put("blockCode", code);
        onCallService.save(toEntity(body));
        return ResponseEntity.ok(
                onCallRepository.findProjectedById(new OnCallId(nurseId, floor, code)).orElseThrow());
    }

    @DeleteMapping("/{nurseId}/{floor}/{code}")
    public ResponseEntity<String> delete(@PathVariable Integer nurseId,
                                          @PathVariable Integer floor,
                                          @PathVariable Integer code) {
        onCallService.delete(new OnCallId(nurseId, floor, code));
        return ResponseEntity.ok("OnCall record deleted.");
    }

    // -----------------------------------------------------------------------
    //  Private helper
    // -----------------------------------------------------------------------

    private OnCall toEntity(Map<String, Object> body) {
        Integer nurseId    = Integer.parseInt(body.get("nurseId").toString());
        Integer blockFloor = Integer.parseInt(body.get("blockFloor").toString());
        Integer blockCode  = Integer.parseInt(body.get("blockCode").toString());

        OnCallId id = new OnCallId(nurseId, blockFloor, blockCode);
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new RuntimeException("Nurse not found: " + nurseId));
        Block block = blockRepository.findById(new BlockId(blockFloor, blockCode))
                .orElseThrow(() -> new RuntimeException("Block not found"));

        LocalDateTime start = body.get("onCallStart") != null
                ? LocalDateTime.parse(body.get("onCallStart").toString().replace(" ", "T")) : null;
        LocalDateTime end = body.get("onCallEnd") != null
                ? LocalDateTime.parse(body.get("onCallEnd").toString().replace(" ", "T")) : null;

        return new OnCall(id, nurse, block, start, end);
    }
}
