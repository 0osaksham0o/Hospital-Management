package com.hospital.service;

import com.hospital.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomService {
    List<Room> getAll();
    Page<Room> getAll(Pageable pageable);
    Room getById(Integer roomNumber);
    boolean existsById(Integer roomNumber);
    Room save(Room room);
    void delete(Integer roomNumber);

    // By Type
    List<Room> getByType(String roomType);
    List<Room> searchByType(String keyword);
    List<String> getAllDistinctRoomTypes();

    // By Availability
    List<Room> getByAvailability(Boolean unavailable);
    long countByAvailability(Boolean unavailable);

    // By Block
    List<Room> getByFloor(Integer blockFloor);
    List<Room> getByBlockCode(Integer blockCode);
    List<Room> getByBlock(Integer blockFloor, Integer blockCode);

    // Combined
    List<Room> getByTypeAndAvailability(String roomType, Boolean unavailable);
    List<Room> getByFloorAndAvailability(Integer blockFloor, Boolean unavailable);

    // Sorted
    List<Room> getAllOrderedByRoomNumber();
}
