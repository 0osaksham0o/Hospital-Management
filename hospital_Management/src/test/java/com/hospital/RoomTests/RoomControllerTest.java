package com.hospital.RoomTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.controller.RoomController;
import com.hospital.entity.Room;
import com.hospital.projection.RoomProjection;
import com.hospital.repository.RoomRepository;
import com.hospital.service.RoomService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

    
    @Autowired private MockMvc mockMvc;
    @MockBean  private RoomService roomService;
    @MockBean  private RoomRepository roomRepository;
    @Autowired private ObjectMapper objectMapper;

    // ── Concrete projection (Jackson-serializable) ─────────────────────────────
    private RoomProjection projection(int number, String type, int floor, int code, boolean unavailable) {
        return new RoomProjection() {
            public Integer getRoomNumber()  { return number;      }
            public String  getRoomType()    { return type;        }
            public Integer getBlockFloor()  { return floor;       }
            public Integer getBlockCode()   { return code;        }
            public Boolean getUnavailable() { return unavailable; }
        };
    }

    // ── Tests ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should return paginated rooms")
    void testGetAll() throws Exception {

        Page<RoomProjection> page = new PageImpl<>(
                List.of(projection(101, "ICU", 1, 1, false)),
                PageRequest.of(0, 5), 1);

        when(roomRepository.findAllProjectedBy(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return room by number")
    void testGetById() throws Exception {

        when(roomRepository.findProjectedByRoomNumber(101))
                .thenReturn(Optional.of(projection(101, "ICU", 1, 1, false)));

        mockMvc.perform(get("/api/rooms/101"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 404 when room not found")
    void testGetByIdNotFound() throws Exception {

        when(roomRepository.findProjectedByRoomNumber(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/rooms/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create room successfully")
    void testCreateRoom() throws Exception {

        Room room = new Room();
        room.setRoomNumber(201);
        room.setRoomType("General");
        room.setBlockFloor(2);
        room.setBlockCode(2);
        room.setUnavailable(false);

        when(roomService.existsById(201)).thenReturn(false);
        when(roomService.save(any(Room.class))).thenReturn(room);
        when(roomRepository.findProjectedByRoomNumber(201))
                .thenReturn(Optional.of(projection(201, "General", 2, 2, false)));

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return 409 when room already exists")
    void testCreateConflict() throws Exception {

        Room room = new Room();
        room.setRoomNumber(101);
        room.setRoomType("ICU");
        room.setBlockFloor(1);
        room.setBlockCode(1);
        room.setUnavailable(false);

        when(roomService.existsById(101)).thenReturn(true);

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should return 400 when room number is missing")
    void testCreateMissingRoomNumber() throws Exception {

        Room room = new Room();
        room.setRoomType("General");
        room.setBlockFloor(1);
        room.setBlockCode(1);
        // roomNumber intentionally omitted

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should update room")
    void testUpdateRoom() throws Exception {

        Room room = new Room();
        room.setRoomType("Private");
        room.setBlockFloor(3);
        room.setBlockCode(3);
        room.setUnavailable(true);

        when(roomService.getById(101)).thenReturn(room);
        when(roomService.save(any(Room.class))).thenReturn(room);
        when(roomRepository.findProjectedByRoomNumber(101))
                .thenReturn(Optional.of(projection(101, "Private", 3, 3, true)));

        mockMvc.perform(put("/api/rooms/101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should delete room")
    void testDeleteRoom() throws Exception {

        doNothing().when(roomService).delete(101);

        mockMvc.perform(delete("/api/rooms/101"))
                .andExpect(status().isOk());
    }
}
