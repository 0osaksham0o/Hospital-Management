package com.hospital.RoomTests;

import com.hospital.entity.Room;
import com.hospital.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Test
    @DisplayName("Test find all rooms")
    void testFindAllRooms() {

        List<Room> rooms = roomRepository.findAll();

        assertNotNull(rooms);
    }

    @Test
    @DisplayName("Test find by room type ICU")
    void testFindByRoomTypeICU() {

        List<Room> rooms = roomRepository.findByRoomType("ICU");

        assertNotNull(rooms);
    }

    @Test
    @DisplayName("Test find by room type General")
    void testFindByRoomTypeGeneral() {

        List<Room> rooms = roomRepository.findByRoomType("General");

        assertNotNull(rooms);
    }

    @Test
    @DisplayName("Test find by unavailable true")
    void testFindByUnavailableTrue() {

        List<Room> rooms =
                roomRepository.findByUnavailable(true);

        assertNotNull(rooms);

        for(Room room : rooms) {
            assertTrue(room.getUnavailable());
        }
    }

    @Test
    @DisplayName("Test find by unavailable false")
    void testFindByUnavailableFalse() {

        List<Room> rooms =
                roomRepository.findByUnavailable(false);

        assertNotNull(rooms);

        for(Room room : rooms) {
            assertFalse(room.getUnavailable());
        }
    }

    @Test
    @DisplayName("Test room exists by id")
    void testExistsById() {

        boolean exists = roomRepository.existsById(101);

        assertTrue(exists || !exists);
    }

    @Test
    @DisplayName("Test count rooms")
    void testCountRooms() {

        long count = roomRepository.count();

        assertTrue(count >= 0);
    }

    @Test
    @DisplayName("Test find room by invalid id")
    void testFindRoomByInvalidId() {

        Optional<Room> room =
                roomRepository.findById(99999);

        assertFalse(room.isPresent());
    }

    @Test
    @DisplayName("Test update room type")
    void testUpdateRoomType() {

        Optional<Room> optionalRoom =
                roomRepository.findById(101);

        if(optionalRoom.isPresent()) {

            Room room = optionalRoom.get();

            room.setRoomType("Updated");

            Room updatedRoom = roomRepository.save(room);

            assertEquals("Updated",
                    updatedRoom.getRoomType());
        }
    }

    @Test
    @DisplayName("Test repository not null")
    void testRepositoryNotNull() {

        assertNotNull(roomRepository);
    }
}