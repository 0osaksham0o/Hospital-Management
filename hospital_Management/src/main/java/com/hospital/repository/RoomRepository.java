package com.hospital.repository;
import com.hospital.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Repository for Room entity.
 * POST, PUT, DELETE, PATCH handled by RoomController.
 */
@RepositoryRestResource(path = "rooms")
public interface RoomRepository extends JpaRepository<Room, Integer> {


    /** Find all rooms of a specific type. */
    List<Room> findByRoomType(String roomType);



    List<Room> findByUnavailable(Boolean unavailable);




}
