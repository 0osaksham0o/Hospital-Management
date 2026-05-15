package com.hospital.controller;

import com.hospital.entity.Block;
import com.hospital.entity.BlockId;
import com.hospital.projection.BlockProjection;
import com.hospital.repository.BlockRepository;
import com.hospital.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Block — full CRUD.
 * GET endpoints return BlockProjection (flat scalars from embedded id).
 * Write body: { "blockFloor": 1, "blockCode": 2 }
 */
@RestController
@RequestMapping("/api/blocks")
@CrossOrigin(origins = "*")
public class BlockController {

    @Autowired private BlockService blockService;
    @Autowired private BlockRepository blockRepository;

    @GetMapping
    public ResponseEntity<Page<BlockProjection>> getAll(@PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(blockRepository.findAllProjectedBy(pageable));
    }

    @GetMapping("/{floor}/{code}")
    public ResponseEntity<BlockProjection> getById(@PathVariable Integer floor,
                                                   @PathVariable Integer code) {
        return blockRepository.findProjectedByIdBlockFloorAndIdBlockCode(floor, code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BlockProjection> create(@RequestBody java.util.Map<String, Object> body) {
        Integer floor = Integer.parseInt(body.get("blockFloor").toString());
        Integer code  = Integer.parseInt(body.get("blockCode").toString());
        blockService.save(new Block(new BlockId(floor, code)));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(blockRepository.findProjectedByIdBlockFloorAndIdBlockCode(floor, code).orElseThrow());
    }

    @DeleteMapping("/{floor}/{code}")
    public ResponseEntity<String> delete(@PathVariable Integer floor, @PathVariable Integer code) {
        blockService.delete(new BlockId(floor, code));
        return ResponseEntity.ok("Block [floor=" + floor + ", code=" + code + "] deleted.");
    }
}
