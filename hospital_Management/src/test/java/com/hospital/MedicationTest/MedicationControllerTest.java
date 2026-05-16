package com.hospital.MedicationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.controller.MedicationController;
import com.hospital.entity.Medication;
import com.hospital.projection.MedicationProjection;
import com.hospital.repository.MedicationRepository;
import com.hospital.service.MedicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.data.domain.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicationController.class)
class MedicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicationService medicationService;

    @MockBean
    private MedicationRepository medicationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return paginated medications")
    void testGetAll() throws Exception {

        MedicationProjection projection =
                Mockito.mock(MedicationProjection.class);

        Page<MedicationProjection> page =
                new PageImpl<>(
                        List.of(projection),
                        PageRequest.of(0, 5),
                        1
                );

        when(medicationRepository.findAllProjectedBy(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/medications"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return medication by code")
    void testGetById() throws Exception {

        MedicationProjection projection =
                Mockito.mock(MedicationProjection.class);

        when(medicationRepository.findProjectedByCode(1001))
                .thenReturn(Optional.of(projection));

        mockMvc.perform(get("/api/medications/1001"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 404 when medication not found")
    void testGetByIdNotFound() throws Exception {

        when(medicationRepository.findProjectedByCode(1001))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/medications/1001"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create medication")
    void testCreateMedication() throws Exception {

        Medication medication = new Medication(
                1001,
                "Paracetamol",
                "Cipla",
                "Pain relief"
        );

        MedicationProjection projection =
                Mockito.mock(MedicationProjection.class);

        when(medicationService.existsById(1001))
                .thenReturn(false);

        when(medicationRepository.findProjectedByCode(1001))
                .thenReturn(Optional.of(projection));

        doNothing().when(medicationService)
                .save(any(Medication.class));

        mockMvc.perform(
                        post("/api/medications")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(medication))
                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should update medication")
    void testUpdateMedication() throws Exception {

        Medication medication = new Medication(
                1001,
                "Updated Medicine",
                "Sun Pharma",
                "Updated description"
        );

        MedicationProjection projection =
                Mockito.mock(MedicationProjection.class);

        when(medicationService.getById(1001))
                .thenReturn(medication);

        when(medicationRepository.findProjectedByCode(1001))
                .thenReturn(Optional.of(projection));

        doNothing().when(medicationService)
                .save(any(Medication.class));

        mockMvc.perform(
                        put("/api/medications/1001")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(medication))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should delete medication")
    void testDeleteMedication() throws Exception {

        doNothing().when(medicationService)
                .delete(1001);

        mockMvc.perform(delete("/api/medications/1001"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Medication 1001 deleted successfully."
                ));
    }

    @Test
    @DisplayName("Should return bad request when name is blank")
    void testCreateMedicationWithoutName() throws Exception {

        Medication medication = new Medication(
                1001,
                "",
                "Cipla",
                "Description"
        );

        when(medicationService.existsById(1001))
                .thenReturn(false);

        mockMvc.perform(
                        post("/api/medications")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(medication))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return bad request when brand is blank")
    void testCreateMedicationWithoutBrand() throws Exception {

        Medication medication = new Medication(
                1001,
                "Paracetamol",
                "",
                "Description"
        );

        when(medicationService.existsById(1001))
                .thenReturn(false);

        mockMvc.perform(
                        post("/api/medications")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(medication))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return conflict when medication already exists")
    void testCreateExistingMedication() throws Exception {

        Medication medication = new Medication(
                1001,
                "Paracetamol",
                "Cipla",
                "Description"
        );

        when(medicationService.existsById(1001))
                .thenReturn(true);

        mockMvc.perform(
                        post("/api/medications")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(medication))
                )
                .andExpect(status().isConflict());
    }
}
