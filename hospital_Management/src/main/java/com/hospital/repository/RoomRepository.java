package com.hospital.repository;

import com.hospital.entity.Room;
import com.hospital.projection.RoomProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Room entity.
 * POST, PUT, DELETE, PATCH handled by RoomController.
 */
@RepositoryRestResource(path = "rooms")
public interface RoomRepository extends JpaRepository<Room, Integer> {

    // ── Projection-based reads ─────────────────────────────────────────────
    Page<RoomProjection> findAllProjectedBy(Pageable pageable);
    Optional<RoomProjection> findProjectedByRoomNumber(Integer roomNumber);


    List<Room> findByRoomType(String roomType);

    List<Room> findByRoomTypeContainingIgnoreCase(String keyword);


    List<Room> findByUnavailable(Boolean unavailable);

    long countByUnavailable(Boolean unavailable);


    List<Room> findByBlockFloor(Integer blockFloor);

    List<Room> findByBlockCode(Integer blockCode);

    List<Room> findByBlockFloorAndBlockCode(Integer blockFloor, Integer blockCode);


    List<Room> findByRoomTypeAndUnavailable(String roomType, Boolean unavailable);

    List<Room> findByBlockFloorAndUnavailable(Integer blockFloor, Boolean unavailable);

    List<Room> findAllByOrderByRoomNumberAsc();
}
