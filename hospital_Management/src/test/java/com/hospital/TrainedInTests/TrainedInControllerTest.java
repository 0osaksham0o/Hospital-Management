package com.hospital.TrainedInTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.controller.TrainedInController;
import com.hospital.entity.*;
import com.hospital.projection.TrainedInProjection;
import com.hospital.repository.PhysicianRepository;
import com.hospital.repository.ProcedureRepository;
import com.hospital.repository.TrainedInRepository;
import com.hospital.service.TrainedInService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainedInController.class)
class TrainedInControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TrainedInService trainedInService;

    @MockBean
    private TrainedInRepository trainedInRepository;

    @MockBean
    private PhysicianRepository physicianRepository;

    @MockBean
    private ProcedureRepository procedureRepository;

    // --------------------------------------------------------------------
    // Helper
    // --------------------------------------------------------------------

    private TrainedInProjection projection() {

        return new TrainedInProjection() {

            @Override
            public Integer getPhysicianId() {
                return 1;
            }

            @Override
            public Integer getTreatmentCode() {
                return 101;
            }

            @Override
            public LocalDateTime getCertificationDate() {
                return LocalDateTime.parse("2024-01-01T10:00:00");
            }

            @Override
            public LocalDateTime getCertificationExpires() {
                return LocalDateTime.parse("2026-01-01T10:00:00");
            }
        };
    }

    // --------------------------------------------------------------------
    // GET ALL
    // --------------------------------------------------------------------

    @Test
    void getAllTrainedIn_shouldReturnPage() throws Exception {

        when(trainedInRepository.findAllProjectedBy(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(projection())));

        mockMvc.perform(get("/api/trainedin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].physicianId").value(1))
                .andExpect(jsonPath("$.content[0].treatmentCode").value(101));

        verify(trainedInRepository, times(1))
                .findAllProjectedBy(any(Pageable.class));
    }

    // --------------------------------------------------------------------
    // GET BY ID
    // --------------------------------------------------------------------

    @Test
    void getById_shouldReturnTrainedIn() throws Exception {

        TrainedInId id = new TrainedInId(1, 101);

        when(trainedInRepository.findProjectedById(id))
                .thenReturn(Optional.of(projection()));

        mockMvc.perform(get("/api/trainedin/1/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.physicianId").value(1))
                .andExpect(jsonPath("$.treatmentCode").value(101));

        verify(trainedInRepository, times(1))
                .findProjectedById(id);
    }

    @Test
    void getById_whenNotFound_shouldReturn404() throws Exception {

        TrainedInId id = new TrainedInId(1, 999);

        when(trainedInRepository.findProjectedById(id))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/trainedin/1/999"))
                .andExpect(status().isNotFound());

        verify(trainedInRepository, times(1))
                .findProjectedById(id);
    }

    // --------------------------------------------------------------------
    // CREATE
    // --------------------------------------------------------------------

    @Test
    void createTrainedIn_shouldReturnCreatedProjection() throws Exception {

        Physician physician = new Physician();
        Procedure procedure = new Procedure();

        TrainedInId id = new TrainedInId(1, 101);

        TrainedIn saved = new TrainedIn(
                id,
                physician,
                procedure,
                LocalDateTime.parse("2024-01-01T10:00:00"),
                LocalDateTime.parse("2026-01-01T10:00:00")
        );

        when(physicianRepository.findById(1))
                .thenReturn(Optional.of(physician));

        when(procedureRepository.findById(101))
                .thenReturn(Optional.of(procedure));

        when(trainedInService.save(any(TrainedIn.class)))
                .thenReturn(saved);

        when(trainedInRepository.findProjectedById(id))
                .thenReturn(Optional.of(projection()));

        Map<String, Object> body = Map.of(
                "physicianId", 1,
                "treatmentCode", 101,
                "certificationDate", "2024-01-01T10:00:00",
                "certificationExpires", "2026-01-01T10:00:00"
        );

        mockMvc.perform(post("/api/trainedin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.physicianId").value(1))
                .andExpect(jsonPath("$.treatmentCode").value(101));

        verify(trainedInService, times(1))
                .save(any(TrainedIn.class));
    }

    @Test
    void createTrainedIn_whenPhysicianNotFound_shouldReturn500() throws Exception {

        when(physicianRepository.findById(1))
                .thenReturn(Optional.empty());

        Map<String, Object> body = Map.of(
                "physicianId", 1,
                "treatmentCode", 101
        );

        mockMvc.perform(post("/api/trainedin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createTrainedIn_whenProcedureNotFound_shouldReturn500() throws Exception {

        Physician physician = new Physician();

        when(physicianRepository.findById(1))
                .thenReturn(Optional.of(physician));

        when(procedureRepository.findById(101))
                .thenReturn(Optional.empty());

        Map<String, Object> body = Map.of(
                "physicianId", 1,
                "treatmentCode", 101
        );

        mockMvc.perform(post("/api/trainedin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createTrainedIn_withInvalidDate_shouldReturn500() throws Exception {

        Physician physician = new Physician();
        Procedure procedure = new Procedure();

        when(physicianRepository.findById(1))
                .thenReturn(Optional.of(physician));

        when(procedureRepository.findById(101))
                .thenReturn(Optional.of(procedure));

        Map<String, Object> body = Map.of(
                "physicianId", 1,
                "treatmentCode", 101,
                "certificationDate", "INVALID_DATE"
        );

        mockMvc.perform(post("/api/trainedin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isInternalServerError());
    }

    // --------------------------------------------------------------------
    // UPDATE
    // --------------------------------------------------------------------

    @Test
    void updateTrainedIn_shouldReturnUpdatedProjection() throws Exception {

        Physician physician = new Physician();
        Procedure procedure = new Procedure();

        TrainedInId id = new TrainedInId(1, 101);

        TrainedIn existing = new TrainedIn();

        TrainedIn updated = new TrainedIn(
                id,
                physician,
                procedure,
                LocalDateTime.parse("2024-02-01T10:00:00"),
                LocalDateTime.parse("2027-02-01T10:00:00")
        );

        when(trainedInService.getById(id))
                .thenReturn(existing);

        when(physicianRepository.findById(1))
                .thenReturn(Optional.of(physician));

        when(procedureRepository.findById(101))
                .thenReturn(Optional.of(procedure));

        when(trainedInService.save(any(TrainedIn.class)))
                .thenReturn(updated);

        when(trainedInRepository.findProjectedById(id))
                .thenReturn(Optional.of(projection()));

        Map<String, Object> body = Map.of(
                "certificationDate", "2024-02-01T10:00:00",
                "certificationExpires", "2027-02-01T10:00:00"
        );

        mockMvc.perform(put("/api/trainedin/1/101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.physicianId").value(1))
                .andExpect(jsonPath("$.treatmentCode").value(101));

        verify(trainedInService, times(1))
                .save(any(TrainedIn.class));
    }

    @Test
    void updateTrainedIn_whenNotFound_shouldReturn500() throws Exception {

        TrainedInId id = new TrainedInId(1, 101);

        when(trainedInService.getById(id))
                .thenThrow(new RuntimeException("Not found"));

        Map<String, Object> body = Map.of(
                "certificationDate", "2024-02-01T10:00:00"
        );

        mockMvc.perform(put("/api/trainedin/1/101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isInternalServerError());
    }

    // --------------------------------------------------------------------
    // DELETE
    // --------------------------------------------------------------------

    @Test
    void deleteTrainedIn_shouldReturnSuccessMessage() throws Exception {

        TrainedInId id = new TrainedInId(1, 101);

        doNothing().when(trainedInService).delete(id);

        mockMvc.perform(delete("/api/trainedin/1/101"))
                .andExpect(status().isOk())
                .andExpect(content().string("Certification deleted."));

        verify(trainedInService, times(1))
                .delete(id);
    }

    @Test
    void deleteTrainedIn_whenNotFound_shouldReturn500() throws Exception {

        TrainedInId id = new TrainedInId(1, 101);

        doThrow(new RuntimeException("Not found"))
                .when(trainedInService)
                .delete(id);

        mockMvc.perform(delete("/api/trainedin/1/101"))
                .andExpect(status().isInternalServerError());
    }
}