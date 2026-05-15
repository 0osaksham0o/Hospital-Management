package com.hospital.controller;

import com.hospital.entity.Room;
import com.hospital.exception.AlreadyExistsException;
import com.hospital.exception.BadRequestException;
import com.hospital.projection.RoomProjection;
import com.hospital.repository.RoomRepository;
import com.hospital.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Room — full CRUD.
 * GET endpoints return RoomProjection (flat scalars, no entity graph).
 */
@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    @Autowired private RoomService roomService;
    @Autowired private RoomRepository roomRepository;

    @GetMapping
    public ResponseEntity<Page<RoomProjection>> getAll(@PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(roomRepository.findAllProjectedBy(pageable));
    }

    @GetMapping("/{roomNumber}")
    public ResponseEntity<RoomProjection> getById(@PathVariable Integer roomNumber) {
        return roomRepository.findProjectedByRoomNumber(roomNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RoomProjection> create(@RequestBody Room room) {
        if (room.getRoomNumber() == null)
            throw new BadRequestException("Incorrect data: 'roomNumber' is required.");
        if (roomService.existsById(room.getRoomNumber()))
            throw new AlreadyExistsException("Room", room.getRoomNumber());
        roomService.save(room);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomRepository.findProjectedByRoomNumber(room.getRoomNumber()).orElseThrow());
    }

    @PutMapping("/{roomNumber}")
    public ResponseEntity<RoomProjection> update(@PathVariable Integer roomNumber,
                                                 @RequestBody Room room) {
        roomService.getById(roomNumber); // 404 if not found
        room.setRoomNumber(roomNumber);
        roomService.save(room);
        return ResponseEntity.ok(roomRepository.findProjectedByRoomNumber(roomNumber).orElseThrow());
    }

    @DeleteMapping("/{roomNumber}")
    public ResponseEntity<Void> delete(@PathVariable Integer roomNumber) {
        roomService.delete(roomNumber);
        return ResponseEntity.ok().build();
    }
}
