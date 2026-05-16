package com.hospital.StayTests;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.controller.StayController;
import com.hospital.entity.Patient;
import com.hospital.entity.Room;
import com.hospital.entity.Stay;
import com.hospital.projection.StayProjection;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.RoomRepository;
import com.hospital.repository.StayRepository;
import com.hospital.service.StayService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.data.domain.*;

        import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StayController.class)
class StayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StayService stayService;

    @MockBean
    private StayRepository stayRepository;

    @MockBean
    private PatientRepository patientRepository;

    @MockBean
    private RoomRepository roomRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return paginated stays")
    void testGetAll() throws Exception {

        StayProjection projection =
                Mockito.mock(StayProjection.class);

        Page<StayProjection> page =
                new PageImpl<>(
                        java.util.List.of(projection),
                        PageRequest.of(0, 5),
                        1
                );

        when(stayRepository.findAllProjectedBy(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/stays"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return stay by id")
    void testGetById() throws Exception {

        StayProjection projection =
                Mockito.mock(StayProjection.class);

        when(stayRepository.findProjectedByStayId(1))
                .thenReturn(Optional.of(projection));

        mockMvc.perform(get("/api/stays/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 404 when stay not found")
    void testGetByIdNotFound() throws Exception {

        when(stayRepository.findProjectedByStayId(1))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/stays/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create stay")
    void testCreateStay() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("stayId", 1);
        request.put("patientSsn", 101);
        request.put("roomNumber", 201);
        request.put("stayStart", "2026-05-16T10:00");
        request.put("stayEnd", "2026-05-18T10:00");

        Patient patient = new Patient();
        patient.setSsn(101);

        Room room = new Room();
        room.setRoomNumber(201);

        Stay stay = new Stay();
        stay.setStayId(1);
        stay.setPatient(patient);
        stay.setRoom(room);

        StayProjection projection =
                Mockito.mock(StayProjection.class);

        when(patientRepository.findById(101))
                .thenReturn(Optional.of(patient));

        when(roomRepository.findById(201))
                .thenReturn(Optional.of(room));

        when(stayService.save(any(Stay.class)))
                .thenReturn(stay);

        when(stayRepository.findProjectedByStayId(1))
                .thenReturn(Optional.of(projection));

        mockMvc.perform(
                        post("/api/stays")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should update stay")
    void testUpdateStay() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("patientSsn", 101);
        request.put("roomNumber", 201);
        request.put("stayStart", "2026-05-16T10:00");
        request.put("stayEnd", "2026-05-18T10:00");

        Patient patient = new Patient();
        patient.setSsn(101);

        Room room = new Room();
        room.setRoomNumber(201);

        Stay stay = new Stay();
        stay.setStayId(1);

        StayProjection projection =
                Mockito.mock(StayProjection.class);

        when(stayService.getById(1))
                .thenReturn(stay);

        when(patientRepository.findById(101))
                .thenReturn(Optional.of(patient));

        when(roomRepository.findById(201))
                .thenReturn(Optional.of(room));

        when(stayService.save(any(Stay.class)))
                .thenReturn(stay);

        when(stayRepository.findProjectedByStayId(1))
                .thenReturn(Optional.of(projection));

        mockMvc.perform(
                        put("/api/stays/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should delete stay")
    void testDeleteStay() throws Exception {

        doNothing().when(stayService)
                .delete(1);

        mockMvc.perform(delete("/api/stays/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return bad request for invalid datetime format")
    void testCreateStayWithInvalidDateTime() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("stayId", 1);
        request.put("patientSsn", 101);
        request.put("roomNumber", 201);
        request.put("stayStart", "16-05-2026 10:00");
        request.put("stayEnd", "18-05-2026 10:00");

        Patient patient = new Patient();
        patient.setSsn(101);

        Room room = new Room();
        room.setRoomNumber(201);

        when(patientRepository.findById(101))
                .thenReturn(Optional.of(patient));

        when(roomRepository.findById(201))
                .thenReturn(Optional.of(room));

        mockMvc.perform(
                        post("/api/stays")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 404 when patient not found")
    void testCreateStayWithMissingPatient() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("stayId", 1);
        request.put("patientSsn", 999);
        request.put("roomNumber", 201);
        request.put("stayStart", "2026-05-16T10:00");
        request.put("stayEnd", "2026-05-18T10:00");

        when(patientRepository.findById(999))
                .thenReturn(Optional.empty());

        mockMvc.perform(
                        post("/api/stays")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 404 when room not found")
    void testCreateStayWithMissingRoom() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("stayId", 1);
        request.put("patientSsn", 101);
        request.put("roomNumber", 999);
        request.put("stayStart", "2026-05-16T10:00");
        request.put("stayEnd", "2026-05-18T10:00");

        Patient patient = new Patient();
        patient.setSsn(101);

        when(patientRepository.findById(101))
                .thenReturn(Optional.of(patient));

        when(roomRepository.findById(999))
                .thenReturn(Optional.empty());

        mockMvc.perform(
                        post("/api/stays")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());
    }
}