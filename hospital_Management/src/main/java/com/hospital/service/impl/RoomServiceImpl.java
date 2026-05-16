package com.hospital.service.impl;

import com.hospital.entity.Room;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.RoomRepository;
import com.hospital.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository repo;

    @Override public List<Room> getAll()                                              { return repo.findAll(); }
    @Override public Page<Room> getAll(Pageable p)                                    { return repo.findAll(p); }
    @Override public Room getById(Integer num)                                        { return repo.findById(num).orElseThrow(() -> new ResourceNotFoundException("Room not found: " + num)); }
    @Override public boolean existsById(Integer num)                                  { return repo.existsById(num); }
    @Override public Room save(Room r)                                                { return repo.save(r); }
    @Override public void delete(Integer num)                                         { getById(num); repo.deleteById(num); }

    @Override public List<Room> getByType(String type)                                { return repo.findByRoomType(type); }
    @Override public List<Room> searchByType(String kw)                               { return repo.findByRoomTypeContainingIgnoreCase(kw); }
    @Override public List<String> getAllDistinctRoomTypes() {
        return repo.findAll().stream()
                .map(Room::getRoomType)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override public List<Room> getByAvailability(Boolean unavailable)                { return repo.findByUnavailable(unavailable); }
    @Override public long countByAvailability(Boolean unavailable)                    { return repo.countByUnavailable(unavailable); }

    @Override public List<Room> getByFloor(Integer floor)                             { return repo.findByBlockFloor(floor); }
    @Override public List<Room> getByBlockCode(Integer code)                          { return repo.findByBlockCode(code); }
    @Override public List<Room> getByBlock(Integer floor, Integer code)               { return repo.findByBlockFloorAndBlockCode(floor, code); }

    @Override public List<Room> getByTypeAndAvailability(String type, Boolean unav)   { return repo.findByRoomTypeAndUnavailable(type, unav); }
    @Override public List<Room> getByFloorAndAvailability(Integer floor, Boolean unav){ return repo.findByBlockFloorAndUnavailable(floor, unav); }

    @Override public List<Room> getAllOrderedByRoomNumber()                            { return repo.findAllByOrderByRoomNumberAsc(); }
}
